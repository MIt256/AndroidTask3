package com.example.taskthree.vm

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.database.Cursor
import android.provider.ContactsContract
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.taskthree.Contact
import com.example.taskthree.model.ContactDAO
import com.example.taskthree.model.ContactEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {
    private var contactList: MutableLiveData<ArrayList<Contact>> = MutableLiveData()
    private var contactToDisplay: MutableLiveData<Contact> = MutableLiveData()

    lateinit var contactDao: ContactDAO

    fun getContactsFromDB(): ArrayList<Contact> {
        val contactList: ArrayList<Contact> = ArrayList()
        val contactsBD = contactDao.getContacts()
        for (contact in contactsBD) {
            contactList.add(
                Contact(
                    contact.firstName,
                    contact.familyName,
                    contact.phone,
                    contact.email
                )
            )
        }
        return contactList
    }

    fun insertContactToDB(pos: Int) {
        CoroutineScope(Dispatchers.Default).launch {
            contactDao.insertContact(
                ContactEntity(
                    0,
                    contactList.value?.get(pos)?.firstName,
                    contactList.value?.get(pos)?.familyName,
                    contactList.value?.get(pos)?.number,
                    contactList.value?.get(pos)?.email
                )
            )
        }
    }

    fun getContactByNumber(number: String): String {
        val name = contactDao.getNameByNumber(number)
        return "${name.firstName ?: ""} ${name.familyName ?: ""}"
    }

    fun getContact() = contactToDisplay
    fun updateContact(contact: Contact) {
        contactToDisplay.value = contact
    }

    fun getListContacts() = contactList

    @SuppressLint("Range")
    fun updateListContacts(cr: ContentResolver) {
        val defaultContactList: ArrayList<Contact> = ArrayList()
        CoroutineScope(Dispatchers.IO).launch {
            val cur = cr.query(
                ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null
            )
            if (cur!!.count > 0) {
                while (cur.moveToNext()) {
                    val id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID))
                    var firstName: String? = null
                    var familyName: String? = null
                    val nameCursor = cr.query(
                        ContactsContract.Data.CONTENT_URI,
                        null,
                        ContactsContract.Data.MIMETYPE + " = ? AND " + ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID + " = " + id,
                        arrayOf(ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE),
                        null
                    )

                    if (nameCursor!!.moveToNext()) {
                        firstName =
                            nameCursor.getString(nameCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME))
                        familyName =
                            nameCursor.getString(nameCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME))
                        nameCursor.close()
                    }

                    //get phone number
                    var phone: String? = null
                    val pCur: Cursor = cr.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf<String>(id),
                        null
                    )!!
                    while (pCur.moveToNext()) {
                        phone =
                            pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    }
                    pCur.close()
                    // get email
                    var email: String? = null
                    val emailCur: Cursor = cr.query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                        arrayOf<String>(id),
                        null
                    )!!
                    while (emailCur.moveToNext()) {
                        email =
                            emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA))
                    }
                    defaultContactList.add(Contact(firstName, familyName, phone, email))
                }
            }
            cur.close()
            withContext(Dispatchers.Main) { contactList.value = defaultContactList }
        }

    }
}
