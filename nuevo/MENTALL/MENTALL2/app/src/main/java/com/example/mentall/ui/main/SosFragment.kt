package com.example.mentall.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mentall.data.api.RetrofitClient
import com.example.mentall.data.local.DatabaseHelper
import com.example.mentall.data.models.Contact
import com.example.mentall.data.models.ScheduledCall
import com.example.mentall.data.models.ScheduledCallRequest
import com.example.mentall.data.prefs.SessionManager
import com.example.mentall.databinding.FragmentSosBinding
import com.example.mentall.utils.AlarmScheduler
import com.example.mentall.utils.Constants
import kotlinx.coroutines.launch

class SosFragment : Fragment() {

    private var _binding: FragmentSosBinding? = null
    private val binding get() = _binding!!

    private lateinit var sessionManager: SessionManager
    private lateinit var alarmScheduler: AlarmScheduler
    private lateinit var callsAdapter: ScheduledCallsAdapter
    private lateinit var databaseHelper: DatabaseHelper

    private var contacts: List<Contact> = emptyList()
    private var scheduledCalls = listOf<ScheduledCall>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())
        alarmScheduler = AlarmScheduler(requireContext())
        databaseHelper = DatabaseHelper(requireContext())

        // ✅ Cargar contactos SQLite en spinner y autorrellenar teléfono
        loadContactsIntoSpinner()

        setupEmergencyButtons()
        setupScheduleForm()
        setupRecyclerView()
        loadScheduledCalls()
    }

    private fun loadContactsIntoSpinner() {
        val user = sessionManager.getUser()
        if (user == null) {
            Toast.makeText(requireContext(), "Sesión no válida", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = user.idUsuario
        contacts = databaseHelper.getContacts(userId)

        if (contacts.isEmpty()) {
            Toast.makeText(
                requireContext(),
                "Añade al menos un contacto de emergencia desde el perfil",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        val displayNames = contacts.map { "${it.nombre} (${it.telefono})" }

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            displayNames
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spContacts.adapter = adapter

        // ✅ Al seleccionar un contacto, rellena el teléfono automáticamente
        binding.spContacts.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val c = contacts[position]
                binding.etTelefono.setText(c.telefono)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // ✅ Preseleccionar el primero
        binding.etTelefono.setText(contacts.first().telefono)
    }

    private fun setupEmergencyButtons() {
        binding.btn112.setOnClickListener {
            makeCall(Constants.TELEFONO_EMERGENCIAS)
        }

        binding.btnEsperanza.setOnClickListener {
            makeCall(Constants.TELEFONO_ESPERANZA)
        }
    }

    private fun makeCall(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        startActivity(intent)
    }

    private fun setupScheduleForm() {
        val dias = listOf(
            "Todos los días",
            "Lunes",
            "Martes",
            "Miércoles",
            "Jueves",
            "Viernes",
            "Sábado",
            "Domingo"
        )

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, dias)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerDia.adapter = adapter

        binding.btnProgramar.setOnClickListener {
            scheduleCall()
        }
    }

    private fun scheduleCall() {
        // ✅ Si hay contactos, usa el seleccionado (y su teléfono)
        if (contacts.isNotEmpty()) {
            val selected = contacts[binding.spContacts.selectedItemPosition]
            binding.etTelefono.setText(selected.telefono)
        }

        val telefono = binding.etTelefono.text.toString().trim()
        val hora = binding.etHora.text.toString().trim()
        val diaIndex = binding.spinnerDia.selectedItemPosition

        if (telefono.isEmpty()) {
            Toast.makeText(context, "Ingresa un teléfono", Toast.LENGTH_SHORT).show()
            return
        }

        if (hora.isEmpty() || !hora.matches(Regex("\\d{2}:\\d{2}"))) {
            Toast.makeText(context, "Formato de hora inválido (HH:MM)", Toast.LENGTH_SHORT).show()
            return
        }

        val diaSemana = if (diaIndex == 0) null else diaIndex
        val userId = sessionManager.getUserId()

        if (userId == -1) {
            Toast.makeText(context, "Error: Usuario no encontrado", Toast.LENGTH_SHORT).show()
            return
        }

        val request = ScheduledCallRequest(
            idUsuario = userId,
            telefono = telefono,
            motivo = "Llamada programada desde la app",
            diaSemana = diaSemana,
            diaMes = null,
            hora = hora
        )

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.createScheduledCall(request)

                if (response.isSuccessful && response.body()?.success == true) {
                    val llamadaData = response.body()?.data

                    if (llamadaData?.idLlamada != null) {
                        alarmScheduler.scheduleCall(
                            id = llamadaData.idLlamada,
                            telefono = telefono,
                            diaSemana = diaSemana,
                            hora = hora
                        )
                    }

                    Toast.makeText(context, "Llamada programada exitosamente", Toast.LENGTH_SHORT).show()
                    loadScheduledCalls()
                } else {
                    Toast.makeText(context, "Error al programar llamada", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error de conexión: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupRecyclerView() {
        callsAdapter = ScheduledCallsAdapter(emptyList()) { _, isActive ->
            Toast.makeText(
                requireContext(),
                if (isActive) "Activada" else "Desactivada",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.recyclerLlamadas.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerLlamadas.adapter = callsAdapter
    }

    private fun loadScheduledCalls() {
        val userId = sessionManager.getUserId()
        if (userId == -1) return

        binding.progressBar.visibility = View.VISIBLE
        binding.tvNoLlamadas.visibility = View.GONE

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.listScheduledCalls(userId)

                binding.progressBar.visibility = View.GONE

                if (response.isSuccessful && response.body()?.success == true) {
                    val data = response.body()?.data
                    if (data != null && data.llamadas.isNotEmpty()) {
                        scheduledCalls = data.llamadas
                        displayScheduledCalls(scheduledCalls)
                    } else {
                        binding.tvNoLlamadas.visibility = View.VISIBLE
                    }
                } else {
                    binding.tvNoLlamadas.visibility = View.VISIBLE
                }
            } catch (e: Exception) {
                binding.progressBar.visibility = View.GONE
                binding.tvNoLlamadas.visibility = View.VISIBLE
                Toast.makeText(context, "Error al cargar llamadas", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun displayScheduledCalls(calls: List<ScheduledCall>) {
        callsAdapter.submitList(calls)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
