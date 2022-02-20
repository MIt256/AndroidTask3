package com.example.taskthree

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.*
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskthree.databinding.ActivityMainBinding
import android.widget.Toast

import android.provider.ContactsContract.CommonDataKinds.Email

import android.database.Cursor
import androidx.room.Room
import com.example.taskthree.room.ContactDAO
import com.example.taskthree.room.ContactDB
import com.example.taskthree.room.ContactEntity


import kotlinx.coroutines.*


class MainActivity : AppCompatActivity(){
    lateinit var binding: ActivityMainBinding
    lateinit var adapter:ContactAdapter
    lateinit var contactDao:ContactDAO
    lateinit var sp:SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sp = getSharedPreferences("NUMBER", Context.MODE_PRIVATE)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.contactListView.layoutManager = LinearLayoutManager(this)

        //click listener 2 button
        binding.buttonShowDB.setOnClickListener {
            val dialog = ContactFragment(contactDao,sp)
            dialog.show(supportFragmentManager, "custom")
        }
        //init DB
        val db = Room.databaseBuilder(
            applicationContext,
            ContactDB::class.java, "contact_db"
        ).build()

        contactDao = db.contactDao()
        //click listener to save element

        adapter = ContactAdapter(){
            Toast.makeText(binding.root.context, R.string.success, Toast.LENGTH_SHORT).show()
            CoroutineScope(Dispatchers.Default).launch{
                contactDao.insertContact(ContactEntity(
                    0,adapter.contactList[it].name,adapter.contactList[it].number,adapter.contactList[it].email))

            }
        }

        binding.contactListView.adapter = adapter
        checkPermission(READ_CONTACTS, binding.root)

        setContentView(binding.root)
    }

    fun btnSPClick(view: View){
        Toast.makeText(binding.root.context, sp.getString("NUMBER","sp is empty"), Toast.LENGTH_SHORT).show()
    }
    //load contacts
    @SuppressLint("Range", "Recycle")
    fun btnChooseClick(view: View) {
        Toast.makeText(this, com.example.taskthree.R.string.load, Toast.LENGTH_SHORT).show()
        CoroutineScope(Dispatchers.IO).launch {
            val contactList: ArrayList<Contact> = ArrayList()
            var name:String? = null
            var phone:String? = null
            var email:String? = null
            val cr = contentResolver

            val cur = cr.query(
                Contacts.CONTENT_URI,
                null, null, null, null
            )
            if (cur!!.count > 0) {
                while (cur.moveToNext()) {
                    val id = cur.getString(cur.getColumnIndex(Contacts._ID))
                    name = cur.getString(cur.getColumnIndex(Contacts.DISPLAY_NAME))
                    //get phone number
                    val pCur: Cursor = cr.query(
                        CommonDataKinds.Phone.CONTENT_URI, null,
                        CommonDataKinds.Phone.CONTACT_ID + " = ?", arrayOf<String>(id), null
                    )!!
                    while (pCur.moveToNext()) {
                        phone = pCur.getString(pCur.getColumnIndex(CommonDataKinds.Phone.NUMBER))
                    }
                    pCur.close()
                    // get email
                    val emailCur: Cursor = cr.query(
                        Email.CONTENT_URI,
                        null,
                        Email.CONTACT_ID + " = ?", arrayOf<String>(id), null
                    )!!
                    while (emailCur.moveToNext()) {
                        email = emailCur.getString(emailCur.getColumnIndex(Email.DATA))
                    }
                    contactList.add(Contact(name, phone, email))
                    phone = null
                    email = null
                }
            }
            withContext(Dispatchers.Main) { adapter.addToList(contactList) }
        }


    }
    //check permission
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (ContextCompat.checkSelfPermission(
                this,
                READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            checkPermission(READ_CONTACTS, binding.root)
        }
    }

}