package com.example.taskthree

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.*
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskthree.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val adapter = ContactAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.contactListView.layoutManager = LinearLayoutManager(this)
        binding.contactListView.adapter = adapter

        checkPermission(READ_CONTACTS,binding.root)
        setContentView(binding.root)
    }


    @SuppressLint("Range")
    fun btnShowClick(view: View){
        val contactList : ArrayList<Contact> = ArrayList()
        val contacts = contentResolver.query(
            CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
        null)
        while (contacts!!.moveToNext()){
            val name = contacts.getString(contacts.getColumnIndex(CommonDataKinds.Phone.DISPLAY_NAME))
            val number =  contacts.getString(contacts.getColumnIndex(CommonDataKinds.Phone.NUMBER))

            val obj = Contact(name,number)

            contactList.add(obj)
        }
        adapter.addToList(contactList)
        contacts.close()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (ContextCompat.checkSelfPermission(this, READ_CONTACTS) == PackageManager.PERMISSION_GRANTED){
            checkPermission(READ_CONTACTS,binding.root)
        }
    }
}