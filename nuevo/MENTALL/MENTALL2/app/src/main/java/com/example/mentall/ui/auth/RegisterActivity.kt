package com.example.mentall.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mentall.MainActivity
import com.example.mentall.data.api.RetrofitClient
import com.example.mentall.data.models.RegisterRequest
import com.example.mentall.data.prefs.SessionManager
import com.example.mentall.databinding.FragmentRegisterBinding
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnRegister.setOnClickListener {
            val nombre = binding.etNombre.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (validateInput(nombre, email, password)) {
                performRegister(nombre, email, password)
            }
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun validateInput(nombre: String, email: String, password: String): Boolean {
        if (nombre.isEmpty()) {
            binding.etNombre.error = "El nombre es requerido"
            return false
        }

        if (email.isEmpty()) {
            binding.etEmail.error = "El email es requerido"
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Email no válido"
            return false
        }

        if (password.isEmpty()) {
            binding.etPassword.error = "La contraseña es requerida"
            return false
        }

        if (password.length < 6) {
            binding.etPassword.error = "Mínimo 6 caracteres"
            return false
        }

        return true
    }

    private fun performRegister(nombre: String, email: String, password: String) {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnRegister.isEnabled = false

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.register(
                    RegisterRequest(nombre, email, password)
                )

                binding.progressBar.visibility = View.GONE
                binding.btnRegister.isEnabled = true

                if (response.isSuccessful && response.body()?.success == true) {
                    val user = response.body()?.data
                    if (user != null) {
                        sessionManager.saveUserSession(user)
                        Toast.makeText(
                            this@RegisterActivity,
                            "Cuenta creada exitosamente. Bienvenido ${user.nombre}",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Ir a MainActivity
                        startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                        finish()
                    }
                } else {
                    val message = response.body()?.message ?: "Error al crear la cuenta"
                    Toast.makeText(this@RegisterActivity, message, Toast.LENGTH_LONG).show()
                }

            } catch (e: Exception) {
                binding.progressBar.visibility = View.GONE
                binding.btnRegister.isEnabled = true
                Toast.makeText(
                    this@RegisterActivity,
                    "Error de conexión: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}