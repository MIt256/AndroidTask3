package com.example.taskthree

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.content.SharedPreferences
import android.widget.Toast
import com.example.taskthree.databinding.DialogBinding
import com.example.taskthree.room.ContactDAO
import com.example.taskthree.room.ContactEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ContactFragment(var contactDao: ContactDAO,val sp:SharedPreferences): DialogFragment() {

    private lateinit var adapter: ContactAdapter
    lateinit var binding: DialogBinding
    lateinit var users: List<ContactEntity>
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogBinding.inflate(layoutInflater)
        binding.rv.layoutManager = LinearLayoutManager(activity)
        adapter = ContactAdapter(){
            Toast.makeText(context, com.example.taskthree.R.string.success, Toast.LENGTH_SHORT).show()
            sp.edit().putString("NUMBER",adapter.contactList[it].number).apply()
            dismiss()
        }

        CoroutineScope(Dispatchers.Default).launch{
            val contactList: ArrayList<Contact> = ArrayList()
            users = contactDao.getContacts()
            for(user in users){
                contactList.add(Contact(null, user.phone, null))
            }
            withContext(Dispatchers.Main) { adapter.addToList(contactList) }
        }
        binding.rv.adapter = adapter

        val builder = AlertDialog.Builder(activity)
        return builder
            .setView(binding.root)
            .create()
    }

}
