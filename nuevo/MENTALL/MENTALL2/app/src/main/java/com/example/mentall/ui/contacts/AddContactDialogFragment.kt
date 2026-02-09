package com.example.mentall.ui.contacts

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.mentall.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AddContactDialogFragment(
    private val onSave: (name: String, phone: String, desc: String) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val v = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_contact, null)
        val etName = v.findViewById<EditText>(R.id.etName)
        val etPhone = v.findViewById<EditText>(R.id.etPhone)
        val etDesc = v.findViewById<EditText>(R.id.etDesc)

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle("Nuevo contacto")
            .setView(v)
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Guardar") { _, _ ->
                val name = etName.text.toString().trim()
                val phone = etPhone.text.toString().trim()
                val desc = etDesc.text.toString().trim()
                onSave(name, phone, desc)
            }
            .create()
    }
}
