package com.example.mentall.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mentall.data.api.RetrofitClient
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
            // Navegar a EmotionEntryFragment
            (activity as? MainActivity)?.loadFragment(EmotionEntryFragment())
        }
        
        binding.btnViewActivities.setOnClickListener {
            // Navegar a ActivitiesFragment
            (activity as? MainActivity)?.loadFragment(ActivitiesFragment())
        }
        
        binding.btnSos.setOnClickListener {
            // Navegar a SosFragment
            (activity as? MainActivity)?.loadFragment(SosFragment())
        }
    }
    
    private fun loadData() {
        loadMoodStats()
        loadRecommendations()
    }
    
    private fun loadMoodStats() {
        binding.progressStats.visibility = View.VISIBLE
        
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getMoodStats(userId, days = 7)
                
                binding.progressStats.visibility = View.GONE
                
                if (response.isSuccessful && response.body()?.success == true) {
                    val stats = response.body()?.data
                    if (stats != null) {
                        displayStats(stats)
                    }
                } else {
                    binding.tvStatsError.visibility = View.VISIBLE
                    binding.tvStatsError.text = "No hay datos suficientes"
                }
                
            } catch (e: Exception) {
                binding.progressStats.visibility = View.GONE
                binding.tvStatsError.visibility = View.VISIBLE
                binding.tvStatsError.text = "Error al cargar estadísticas"
            }
        }
    }
    
    private fun displayStats(stats: MoodStatsResponse) {
        binding.apply {
            tvTotalRegistros.text = "Registros: ${stats.totalRegistros}"
            tvPromedio.text = "Promedio: ${stats.promedio}"
            
            val ultimo = stats.ultimoRegistro
            if (ultimo != null) {
                tvUltimoMood.text = "Último: ${getMoodLabel(ultimo.valor)}"
                tvUltimaActividad.text = "Actividad: ${ultimo.actividadRealizada ?: "Ninguna"}"
                tvUltimaNota.text = "Nota: ${ultimo.nota ?: "Sin nota"}"
            }
            
            // Mostrar últimos 7 valores
            val valoresText = stats.ultimosValores.joinToString(", ")
            tvUltimosValores.text = "Últimos 7 días: $valoresText"
        }
    }
    
    private fun loadRecommendations() {
        binding.progressRecomendaciones.visibility = View.VISIBLE
        
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getRecommendations(userId)
                
                binding.progressRecomendaciones.visibility = View.GONE
                
                if (response.isSuccessful && response.body()?.success == true) {
                    val data = response.body()?.data
                    if (data != null && data.recomendaciones.isNotEmpty()) {
                        displayRecommendations(data.recomendaciones)
                    } else {
                        binding.tvNoRecomendaciones.visibility = View.VISIBLE
                    }
                }
                
            } catch (e: Exception) {
                binding.progressRecomendaciones.visibility = View.GONE
                binding.tvNoRecomendaciones.visibility = View.VISIBLE
            }
        }
    }
    
    private fun displayRecommendations(recomendaciones: List<Recommendation>) {
        // Aquí irían las recomendaciones en un RecyclerView
        // Por simplicidad, mostrar solo la primera
        val primera = recomendaciones.first()
        binding.tvRecomendacion1.text = "${primera.titulo} (${primera.duracionMin} min)"
        binding.tvRecomendacion1Desc.text = primera.descripcion
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
