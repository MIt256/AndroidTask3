package com.example.taskthree.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.Toast
import com.example.taskthree.Contact
import com.example.taskthree.R
import com.example.taskthree.databinding.DialogBinding

class DialogFragment(val contacts: ArrayList<Contact>, val clickListener: (Contact) -> Unit) :
    DialogFragment() {

    private lateinit var adapter: ContactAdapter
    private lateinit var binding: DialogBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogBinding.inflate(layoutInflater)
        binding.rv.layoutManager = LinearLayoutManager(activity)

        adapter = ContactAdapter() {
            clickListener(
                Contact(
                    contacts[it].firstName,
                    contacts[it].familyName,
                    contacts[it].number,
                    contacts[it].email
                )
            )
            Toast.makeText(context, R.string.success, Toast.LENGTH_SHORT).show()

        }
        val displayContacts = ArrayList<Contact>()
        for(con in contacts){
            displayContacts.add(Contact(null,null,con.number,null))
        }
        adapter.addToList(displayContacts)
        binding.rv.adapter = adapter

        val builder = AlertDialog.Builder(activity)
        return builder
            .setView(binding.root)
            .create()
    }

    override fun onPause() {
        super.onPause()
        dismiss()
    }

}
