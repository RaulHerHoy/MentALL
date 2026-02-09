package com.example.mentall.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mentall.MainActivity
import com.example.mentall.data.api.RetrofitClient
import com.example.mentall.data.models.MoodStatsResponse
import com.example.mentall.data.models.Recommendation
import com.example.mentall.data.prefs.SessionManager
import com.example.mentall.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var sessionManager: SessionManager
    private var userId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())
        userId = sessionManager.getUserId()

        if (userId == -1) {
            Toast.makeText(context, "Error: Usuario no encontrado", Toast.LENGTH_SHORT).show()
            return
        }

        setupUI()
        loadData()
    }

    private fun setupUI() {
        binding.tvUserName.text = "Hola, ${sessionManager.getUserName()}"

        // Botones de navegación rápida
        binding.btnRegisterMood.setOnClickListener {
            (activity as? MainActivity)?.loadFragment(EmotionEntryFragment())
        }

        binding.btnViewActivities.setOnClickListener {
            (activity as? MainActivity)?.loadFragment(ActivitiesFragment())
        }

        binding.btnSos.setOnClickListener {
            (activity as? MainActivity)?.loadFragment(SosFragment())
        }
    }

    private fun loadData() {
        loadMoodStats()
        loadRecommendations()
    }

    private fun loadMoodStats() {
        // Si la vista ya no existe, no hagas nada
        _binding?.progressStats?.visibility = View.VISIBLE
        _binding?.tvStatsError?.visibility = View.GONE

        // IMPORTANTE: usar el lifecycle de la VISTA (se cancela al destruirse la view)
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getMoodStats(userId, days = 7)

                val b = _binding ?: return@launch
                b.progressStats.visibility = View.GONE

                if (response.isSuccessful && response.body()?.success == true) {
                    val stats = response.body()?.data
                    if (stats != null) {
                        displayStats(stats)
                    } else {
                        b.tvStatsError.visibility = View.VISIBLE
                        b.tvStatsError.text = "No hay datos suficientes"
                    }
                } else {
                    b.tvStatsError.visibility = View.VISIBLE
                    b.tvStatsError.text = "No hay datos suficientes"
                }

            } catch (e: Exception) {
                val b = _binding ?: return@launch
                b.progressStats.visibility = View.GONE
                b.tvStatsError.visibility = View.VISIBLE
                b.tvStatsError.text = "Error al cargar estadísticas"
            }
        }
    }

    private fun displayStats(stats: MoodStatsResponse) {
        val b = _binding ?: return

        b.apply {
            tvTotalRegistros.text = "Registros: ${stats.totalRegistros}"
            tvPromedio.text = "Promedio: ${stats.promedio}"

            val ultimo = stats.ultimoRegistro
            if (ultimo != null) {
                tvUltimoMood.text = "Último: ${getMoodLabel(ultimo.valor)}"
                tvUltimaActividad.text = "Actividad: ${ultimo.actividadRealizada ?: "Ninguna"}"
                tvUltimaNota.text = "Nota: ${ultimo.nota ?: "Sin nota"}"
            } else {
                tvUltimoMood.text = "Último: —"
                tvUltimaActividad.text = "Actividad: —"
                tvUltimaNota.text = "Nota: —"
            }

            // Mostrar últimos valores
            val valoresText = stats.ultimosValores.joinToString(", ")
            tvUltimosValores.text = "Últimos 7 días: $valoresText"
        }
    }

    private fun loadRecommendations() {
        _binding?.progressRecomendaciones?.visibility = View.VISIBLE
        _binding?.tvNoRecomendaciones?.visibility = View.GONE

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getRecommendations(userId)

                val b = _binding ?: return@launch
                b.progressRecomendaciones.visibility = View.GONE

                if (response.isSuccessful && response.body()?.success == true) {
                    val data = response.body()?.data
                    if (data != null && data.recomendaciones.isNotEmpty()) {
                        displayRecommendations(data.recomendaciones)
                    } else {
                        b.tvNoRecomendaciones.visibility = View.VISIBLE
                    }
                } else {
                    b.tvNoRecomendaciones.visibility = View.VISIBLE
                }

            } catch (e: Exception) {
                val b = _binding ?: return@launch
                b.progressRecomendaciones.visibility = View.GONE
                b.tvNoRecomendaciones.visibility = View.VISIBLE
            }
        }
    }

    private fun displayRecommendations(recomendaciones: List<Recommendation>) {
        val b = _binding ?: return

        // Por simplicidad, mostrar solo la primera
        val primera = recomendaciones.first()
        b.tvRecomendacion1.text = "${primera.titulo} (${primera.duracionMin} min)"
        b.tvRecomendacion1Desc.text = primera.descripcion
    }

    private fun getMoodLabel(valor: Int): String {
        return when (valor) {
            1 -> "Muy mal"
            2 -> "Mal"
            3 -> "Regular"
            4 -> "Bien"
            5 -> "Genial"
            else -> "Desconocido"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
