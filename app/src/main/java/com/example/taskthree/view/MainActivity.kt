package com.example.taskthree.view

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskthree.databinding.ActivityMainBinding
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.taskthree.*
import com.example.taskthree.model.ContactDB
import com.example.taskthree.vm.MainViewModel
import kotlinx.coroutines.*
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var adapter: ContactAdapter

    lateinit var sp: SharedPreferences

    private val mainViewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sp = getSharedPreferences(KEY, Context.MODE_PRIVATE)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.contactListView.layoutManager = LinearLayoutManager(this)

        val db = Room.databaseBuilder(
            applicationContext,
            ContactDB::class.java, "contact_db"
        ).build()
        mainViewModel.contactDao = db.contactDao()

        mainViewModel.getListContacts().observe(this, {
            it?.let {
                adapter.addToList(it)
            }
        })
        mainViewModel.getContact().observe(this, {
            it?.let {
                with(binding) {
                    textName.text = "${it.firstName ?: ""} ${it.familyName ?: ""}"
                    textNumber.text = it.number
                    textEmail.text = it.email
                }
            }
        })
        //click listener to save element in DB
        adapter = ContactAdapter() {
            mainViewModel.insertContactToDB(it)
            Toast.makeText(binding.root.context, R.string.success, Toast.LENGTH_SHORT).show()
        }
        binding.contactListView.adapter = adapter
        checkPermission(READ_CONTACTS, binding.root)
        setContentView(binding.root)
    }

    //display dialog
    fun btnShowClick(view: View) {
        CoroutineScope(Dispatchers.Default).launch {
            val contacts = mainViewModel.getContactsFromDB()
            val dialog = DialogFragment(contacts) {
                sp.edit().putString(KEY, it.number).apply()
                mainViewModel.updateContact(it)
            }
            dialog.show(supportFragmentManager, "custom")
        }
    }

    //display Shared Preferences in SnackBAr
    fun btnSPClick(view: View) {
        val snackbar = Snackbar.make(
            view,
            sp.getString(KEY, "No number in SP").toString(),
            Snackbar.LENGTH_LONG
        )
        snackbar.setAction("close", View.OnClickListener { snackbar.dismiss() })
            .show()
    }

    //display contact name in notification
    fun btnNotification(view: View) {
        val phoneNumber = sp.getString(KEY, null)
        if (phoneNumber != null) {
            CoroutineScope(Dispatchers.Default).launch {
                val fullName = mainViewModel.getContactByNumber(phoneNumber)
                val notification = Notification(binding.root.context)
                notification.createNotification(fullName)
            }
        } else Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show()
    }

    //get contacts
    fun btnChooseClick(view: View) {
        if (ContextCompat.checkSelfPermission(this,READ_CONTACTS) == PackageManager.PERMISSION_GRANTED){
        Toast.makeText(this, R.string.load, Toast.LENGTH_SHORT).show()
        mainViewModel.updateListContacts(contentResolver)
        } else Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show()
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

    companion object {
        const val KEY = "NUMBER"
    }

}