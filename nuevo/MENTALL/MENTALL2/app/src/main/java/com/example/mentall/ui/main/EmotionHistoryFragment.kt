package com.example.mentall.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mentall.R
import com.example.mentall.data.api.RetrofitClient
import com.example.mentall.data.prefs.SessionManager
import com.example.mentall.databinding.FragmentEmotionHistoryBinding
import kotlinx.coroutines.launch

class EmotionHistoryFragment : Fragment(R.layout.fragment_emotion_history) {

    private var _binding: FragmentEmotionHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var session: SessionManager
    private lateinit var adapter: MoodHistoryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentEmotionHistoryBinding.bind(view)

        session = SessionManager(requireContext())

        adapter = MoodHistoryAdapter(emptyList())

        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = adapter

        loadHistory()
    }

    private fun loadHistory() {
        val userId = session.getUserId()
        if (userId == -1) return

        binding.progress.visibility = View.VISIBLE
        binding.tvEmpty.visibility = View.GONE

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.listMoods(idUsuario = userId, limit = 50)

                val b = _binding ?: return@launch
                b.progress.visibility = View.GONE

                if (response.isSuccessful && response.body()?.success == true) {
                    val data = response.body()?.data
                    val list = data?.registros ?: emptyList()

                    if (list.isEmpty()) {
                        b.tvEmpty.visibility = View.VISIBLE
                        adapter.submitList(emptyList())
                    } else {
                        b.tvEmpty.visibility = View.GONE
                        adapter.submitList(list)
                    }
                } else {
                    b.tvEmpty.visibility = View.VISIBLE
                    b.tvEmpty.text = "No se pudo cargar el historial."
                }

            } catch (e: Exception) {
                val b = _binding ?: return@launch
                b.progress.visibility = View.GONE
                b.tvEmpty.visibility = View.VISIBLE
                b.tvEmpty.text = "Error de conexión."
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
