package com.example.taskthree

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.taskthree.databinding.ContactBinding

class ContactAdapter: RecyclerView.Adapter<ContactAdapter.ContactHolder>() {

    private var contactList = ArrayList<Contact>()

    class ContactHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ContactBinding.bind(view)
        fun bind(cont: Contact) = with(binding) {
            contactName.text = cont.name
            contactNumber.text = cont.number
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.contact, parent, false)
        return ContactHolder(view)
    }

    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        holder.itemView.setOnClickListener {
            Toast.makeText(holder.binding.root.context, R.string.success, Toast.LENGTH_SHORT).show()
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