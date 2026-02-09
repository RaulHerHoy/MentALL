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
import com.example.mentall.data.models.Activity
import com.example.mentall.databinding.FragmentActivitiesBinding
import kotlinx.coroutines.launch

class ActivitiesFragment : Fragment() {

    private var _binding: FragmentActivitiesBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ActivitiesAdapter
    private var allActivities = listOf<Activity>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActivitiesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearch()
        loadActivities()
    }

    private fun setupRecyclerView() {
        adapter = ActivitiesAdapter(emptyList()) { activity ->
            onActivityStartClick(activity)
        }

        binding.recyclerActivities.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@ActivitiesFragment.adapter
        }
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                filterActivities(s.toString())
            }
        })
    }

    private fun filterActivities(query: String) {
        if (query.isEmpty()) {
            adapter.updateActivities(allActivities)
        } else {
            val filtered = allActivities.filter {
                it.titulo.contains(query, ignoreCase = true) ||
                        it.descripcion.contains(query, ignoreCase = true) ||
                        it.categoria.contains(query, ignoreCase = true)
            }
            adapter.updateActivities(filtered)
        }
    }

    private fun loadActivities() {
        binding.progressBar.visibility = View.VISIBLE
        binding.tvError.visibility = View.GONE

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.listActivities()

                binding.progressBar.visibility = View.GONE

                if (response.isSuccessful && response.body()?.success == true) {
                    val data = response.body()?.data
                    if (data != null) {
                        allActivities = data.actividades
                        adapter.updateActivities(allActivities)
                    }
                } else {
                    binding.tvError.visibility = View.VISIBLE
                    Toast.makeText(
                        context,
                        "Error al cargar actividades",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } catch (e: Exception) {
                binding.progressBar.visibility = View.GONE
                binding.tvError.visibility = View.VISIBLE
                Toast.makeText(
                    context,
                    "Error de conexión: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun onActivityStartClick(activity: Activity) {
        Toast.makeText(
            context,
            "Iniciando: ${activity.titulo}",
            Toast.LENGTH_SHORT
        ).show()
        // Aquí podrías abrir un enlace si activity.enlace no es null
        // O mostrar detalles de la actividad en un dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}