package com.example.taskthree.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.taskthree.Contact
import com.example.taskthree.R
import com.example.taskthree.databinding.ContactBinding

class ContactAdapter(val clickListener: (Int) -> Unit) :
    RecyclerView.Adapter<ContactAdapter.ContactHolder>() {

    var contactList = ArrayList<Contact>()

    class ContactHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ContactBinding.bind(view)
        fun bind(cont: Contact) = with(binding) {
            if (cont.firstName != null || cont.familyName != null) {
                contactName.text = "${cont.firstName ?: ""} ${cont.familyName ?: ""}"
            } else contactName.visibility = View.GONE
            if (cont.number != null) {
                contactNumber.text = cont.number
                contactNumber.visibility = View.VISIBLE
            } else contactNumber.visibility = View.GONE
            if (cont.email != null) {
                contactEmail.text = cont.email
                contactEmail.visibility = View.VISIBLE
            } else contactEmail.visibility = View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.contact, parent, false)
        return ContactHolder(view)
    }

    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        holder.itemView.setOnClickListener {
            clickListener(position)
        }
        holder.bind(contactList[position])
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    fun addToList(items: ArrayList<Contact>) {
        contactList = items
        notifyDataSetChanged()
    }

}