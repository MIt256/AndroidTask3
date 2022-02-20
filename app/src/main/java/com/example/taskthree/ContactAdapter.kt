package com.example.taskthree

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.taskthree.databinding.ContactBinding

class ContactAdapter(val clickListener: (Int) -> Unit): RecyclerView.Adapter<ContactAdapter.ContactHolder>() {

    var contactList = ArrayList<Contact>()
    //private ContactElementListener onDeleteButtonClickListener

    class ContactHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ContactBinding.bind(view)
        fun bind(cont: Contact) = with(binding) {
            if (cont.name != null) contactName.text = cont.name else contactName.visibility = View.GONE
            contactNumber.text = cont.number
            if (cont.email != null && cont.email != "" ) {contactEmail.text = cont.email
            contactEmail.visibility = View.VISIBLE}
            else contactEmail.visibility = View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.contact, parent, false)
        return ContactHolder(view)
    }

    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        holder.itemView.setOnClickListener{
            clickListener(position)
        }
        holder.bind(contactList[position])
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    fun addToList(items: ArrayList<Contact>){
        contactList = items
        notifyDataSetChanged()
    }

}