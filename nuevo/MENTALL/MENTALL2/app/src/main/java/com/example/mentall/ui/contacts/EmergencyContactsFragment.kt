package com.example.mentall.ui.contacts

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mentall.R
import com.example.mentall.data.local.DatabaseHelper
import com.example.mentall.data.models.Contact
import com.example.mentall.data.prefs.SessionManager
import com.example.mentall.databinding.FragmentEmergencyContactsBinding
import kotlinx.coroutines.launch

class EmergencyContactsFragment : Fragment(R.layout.fragment_emergency_contacts) {

    private var _binding: FragmentEmergencyContactsBinding? = null
    private val binding get() = _binding!!

    private lateinit var db: DatabaseHelper
    private lateinit var session: SessionManager
    private var userId: Int = -1

    private lateinit var adapter: ContactsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentEmergencyContactsBinding.bind(view)

        db = DatabaseHelper(requireContext())
        session = SessionManager(requireContext())

        val user = session.getUser()
        if (user == null) {
            Toast.makeText(requireContext(), "Sesión no válida", Toast.LENGTH_SHORT).show()
            requireActivity().finish()
            return
        }
        userId = user.idUsuario

        adapter = ContactsAdapter(onDelete = { c -> deleteContact(c) })

        binding.rvContacts.layoutManager = LinearLayoutManager(requireContext())
        binding.rvContacts.adapter = adapter

        binding.btnAdd.setOnClickListener {
            AddContactDialogFragment { name, phone, desc ->
                if (name.isBlank() || phone.isBlank()) {
                    Toast.makeText(requireContext(), "Nombre y teléfono son obligatorios", Toast.LENGTH_SHORT).show()
                    return@AddContactDialogFragment
                }
                insertContact(name, phone, desc)
            }.show(parentFragmentManager, "addContact")
        }

        refresh()
    }

    private fun refresh() {
        lifecycleScope.launch {
            val list = db.getContacts(userId)
            adapter.submitList(list)
            binding.tvEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun insertContact(name: String, phone: String, desc: String) {
        lifecycleScope.launch {
            val c = Contact(
                idContacto = 0,
                idUsuario = userId,
                nombre = name,
                telefono = phone,
                descripcion = desc,
                esEmergencia = 1,
                orden = 0
            )
            db.insertContact(c, userId)
            refresh()
        }
    }

    private fun deleteContact(c: Contact) {
        lifecycleScope.launch {
            db.deleteContact(c.idContacto)
            refresh()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
