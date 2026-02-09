package com.example.mentall.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mentall.data.api.RetrofitClient
import com.example.mentall.data.models.Skill
import com.example.mentall.data.models.SkillCreateRequest
import com.example.mentall.data.prefs.SessionManager
import com.example.mentall.databinding.FragmentSkillsBinding
import kotlinx.coroutines.launch

class SkillsFragment : Fragment() {

    private var _binding: FragmentSkillsBinding? = null
    private val binding get() = _binding!!

    private lateinit var sessionManager: SessionManager
    private var allSkills = listOf<Skill>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSkillsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())

        setupPublishForm()
        setupRecyclerView()
        setupSearch()
        loadSkills()
    }

    private fun setupPublishForm() {
        binding.btnPublicar.setOnClickListener {
            publishSkill()
        }
    }

    private fun publishSkill() {
        val titulo = binding.etSkillTitle.text.toString().trim()
        val descripcion = binding.etSkillDesc.text.toString().trim()

        if (titulo.isEmpty()) {
            Toast.makeText(context, "Ingresa el título de la habilidad", Toast.LENGTH_SHORT).show()
            return
        }

        val nivel = when (binding.chipGroupNivel.checkedChipId) {
            binding.chipIntermedio.id -> "Intermedio"
            binding.chipAvanzado.id -> "Avanzado"
            else -> "Básico"
        }

        val userId = sessionManager.getUserId()
        if (userId == -1) {
            Toast.makeText(context, "Error: Usuario no encontrado", Toast.LENGTH_SHORT).show()
            return
        }

        binding.progressPublicar.visibility = View.VISIBLE
        binding.btnPublicar.isEnabled = false

        val request = SkillCreateRequest(
            idUsuario = userId,
            titulo = titulo,
            nivel = nivel,
            descripcion = descripcion.ifEmpty { null }
        )

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.createSkill(request)

                binding.progressPublicar.visibility = View.GONE
                binding.btnPublicar.isEnabled = true

                if (response.isSuccessful && response.body()?.success == true) {
                    Toast.makeText(
                        context,
                        "Habilidad publicada exitosamente",
                        Toast.LENGTH_SHORT
                    ).show()

                    // Limpiar formulario
                    binding.etSkillTitle.setText("")
                    binding.etSkillDesc.setText("")
                    binding.chipBasico.isChecked = true

                    // Recargar lista
                    loadSkills()

                } else {
                    val message = response.body()?.message ?: "Error al publicar"
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                }

            } catch (e: Exception) {
                binding.progressPublicar.visibility = View.GONE
                binding.btnPublicar.isEnabled = true
                Toast.makeText(
                    context,
                    "Error de conexión: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerSkills.layoutManager = LinearLayoutManager(context)
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                filterSkills(s.toString())
            }
        })
    }

    private fun filterSkills(query: String) {
        if (query.isEmpty()) {
            displaySkills(allSkills)
        } else {
            val filtered = allSkills.filter {
                it.titulo.contains(query, ignoreCase = true) ||
                        it.descripcion?.contains(query, ignoreCase = true) == true
            }
            displaySkills(filtered)
        }
    }

    private fun loadSkills() {
        binding.progressBar.visibility = View.VISIBLE
        binding.tvNoSkills.visibility = View.GONE

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.listSkills()

                binding.progressBar.visibility = View.GONE

                if (response.isSuccessful && response.body()?.success == true) {
                    val data = response.body()?.data
                    if (data != null && data.skills.isNotEmpty()) {
                        allSkills = data.skills
                        displaySkills(allSkills)
                    } else {
                        binding.tvNoSkills.visibility = View.VISIBLE
                    }
                } else {
                    binding.tvNoSkills.visibility = View.VISIBLE
                }

            } catch (e: Exception) {
                binding.progressBar.visibility = View.GONE
                binding.tvNoSkills.visibility = View.VISIBLE
                Toast.makeText(
                    context,
                    "Error de conexión: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun displaySkills(skills: List<Skill>) {
        // Aquí deberías implementar un SkillsAdapter similar a ActivitiesAdapter
        // Por ahora solo muestro un mensaje
        Toast.makeText(
            context,
            "Cargadas ${skills.size} habilidades",
            Toast.LENGTH_SHORT
        ).show()

        // TODO: Implementar SkillsAdapter
        /*
        val adapter = SkillsAdapter(skills) { skill ->
            // Acción al hacer click en conectar
        }
        binding.recyclerSkills.adapter = adapter
        */
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}