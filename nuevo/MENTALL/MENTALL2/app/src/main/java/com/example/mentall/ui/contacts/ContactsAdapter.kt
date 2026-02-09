package com.example.mentall.ui.contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mentall.R
import com.example.mentall.data.models.Contact

class ContactsAdapter(
    private val onDelete: (Contact) -> Unit
) : RecyclerView.Adapter<ContactsAdapter.VH>() {

    private val items = mutableListOf<Contact>()

    fun submitList(newItems: List<Contact>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(items[position])
    override fun getItemCount(): Int = items.size

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        private val tvName: TextView = v.findViewById(R.id.tvName)
        private val tvPhone: TextView = v.findViewById(R.id.tvPhone)
        private val tvDesc: TextView = v.findViewById(R.id.tvDesc)
        private val btnDelete: Button = v.findViewById(R.id.btnDelete)

        fun bind(c: Contact) {
            tvName.text = c.nombre
            tvPhone.text = c.telefono

            val desc = c.descripcion?.trim().orEmpty()
            if (desc.isNotEmpty()) {
                tvDesc.visibility = View.VISIBLE
                tvDesc.text = desc
            } else {
                tvDesc.visibility = View.GONE
            }

            btnDelete.setOnClickListener { onDelete(c) }
        }
    }
}
