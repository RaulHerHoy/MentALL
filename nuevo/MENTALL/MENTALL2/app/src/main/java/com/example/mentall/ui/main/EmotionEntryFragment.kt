package com.example.mentall.ui.main

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.mentall.MainActivity
import com.example.mentall.data.api.RetrofitClient
import com.example.mentall.data.models.MoodCreateRequest
import com.example.mentall.data.prefs.SessionManager
import com.example.mentall.databinding.FragmentEmotionEntryBinding
import com.example.mentall.utils.Constants
import kotlinx.coroutines.launch

class EmotionEntryFragment : Fragment() {

    private var _binding: FragmentEmotionEntryBinding? = null
    private val binding get() = _binding!!

    private lateinit var sessionManager: SessionManager
    private var currentMoodValue = 3
    private var photoUri: String? = null

    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) openCamera()
        else Toast.makeText(context, "Permiso denegado", Toast.LENGTH_SHORT).show()
    }

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data
            if (uri != null) {
                photoUri = uri.toString()
                binding.tvPhotoStatus.text = "Foto seleccionada ✓"
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmotionEntryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())

        setupMoodSlider()
        setupPhotoButtons()
        setupSaveButton()

        // ✅ BOTÓN "Ver historial"
        binding.btnHistory.setOnClickListener {
            (activity as? MainActivity)?.loadFragment(EmotionHistoryFragment())
        }
    }

    // =========================
    // MOOD UI (colores + label)
    // =========================
    private fun moodColor(value: Int): Int {
        return when (value) {
            1 -> 0xFFE53935.toInt() // Muy mal -> rojo
            2 -> 0xFFFB8C00.toInt() // Mal -> naranja
            3 -> 0xFFFDD835.toInt() // Regular -> amarillo
            4 -> 0xFF43A047.toInt() // Bien -> verde
            5 -> 0xFF7BC8A4.toInt() // Genial -> verde menta
            else -> 0xFF9E9E9E.toInt()
        }
    }

    private fun applyMoodUI(value: Int) {
        val c = moodColor(value)

        // Punto
        binding.sliderMood.thumbTintList = ColorStateList.valueOf(c)

        // Línea activa (hasta el punto)
        binding.sliderMood.trackActiveTintList = ColorStateList.valueOf(c)

        val label = Constants.MOOD_LABELS[value] ?: "Desconocido"
        binding.tvMoodLabel.text = "$label ($value/5)"
    }

    private fun setupMoodSlider() {
        // Inicial
        currentMoodValue = binding.sliderMood.value.toInt()
        applyMoodUI(currentMoodValue)

        binding.sliderMood.addOnChangeListener { _, value, _ ->
            currentMoodValue = value.toInt()
            applyMoodUI(currentMoodValue)
        }
    }

    // =========================
    // FOTO
    // =========================
    private fun setupPhotoButtons() {
        binding.btnCamera.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                openCamera()
            } else {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }

        binding.btnGallery.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            galleryLauncher.launch(intent)
        }
    }

    private fun openCamera() {
        // Aquí implementarías la lógica real de la cámara.
        // Por simplicidad, solo mockeamos.
        photoUri = "mock_camera_uri"
        binding.tvPhotoStatus.text = "Foto tomada ✓"
    }

    // =========================
    // GUARDAR
    // =========================
    private fun setupSaveButton() {
        binding.btnSave.setOnClickListener {
            saveMoodEntry()
        }
    }

    private fun saveMoodEntry() {
        val actividad = binding.etActividad.text.toString().trim()
        val nota = binding.etNota.text.toString().trim()
        val userId = sessionManager.getUserId()

        if (userId == -1) {
            Toast.makeText(context, "Error: Usuario no encontrado", Toast.LENGTH_SHORT).show()
            return
        }

        binding.progressBar.visibility = View.VISIBLE
        binding.btnSave.isEnabled = false

        val request = MoodCreateRequest(
            idUsuario = userId,
            valor = currentMoodValue,
            actividadRealizada = actividad.ifEmpty { null },
            nota = nota.ifEmpty { null },
            imagenUri = photoUri
        )

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.createMood(request)

                binding.progressBar.visibility = View.GONE
                binding.btnSave.isEnabled = true

                if (response.isSuccessful && response.body()?.success == true) {
                    Toast.makeText(
                        context,
                        "Registro guardado exitosamente",
                        Toast.LENGTH_SHORT
                    ).show()

                    clearForm()
                } else {
                    val message = response.body()?.message ?: "Error al guardar"
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                }

            } catch (e: Exception) {
                binding.progressBar.visibility = View.GONE
                binding.btnSave.isEnabled = true
                Toast.makeText(
                    context,
                    "Error de conexión: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun clearForm() {
        binding.sliderMood.value = 3f
        binding.etActividad.setText("")
        binding.etNota.setText("")
        photoUri = null
        binding.tvPhotoStatus.text = "Sin foto"
        currentMoodValue = 3
        applyMoodUI(currentMoodValue)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
