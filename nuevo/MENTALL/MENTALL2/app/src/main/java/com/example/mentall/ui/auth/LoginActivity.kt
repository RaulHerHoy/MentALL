package com.example.mentall.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mentall.MainActivity
import com.example.mentall.data.api.RetrofitClient
import com.example.mentall.data.models.LoginRequest
import com.example.mentall.data.prefs.SessionManager
import com.example.mentall.databinding.FragmentLoginBinding
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    
    private lateinit var binding: FragmentLoginBinding
    private lateinit var sessionManager: SessionManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        sessionManager = SessionManager(this)
        
        // Si ya está logueado, ir a MainActivity
        if (sessionManager.isLoggedIn()) {
            startMainActivity()
            return
        }
        
        setupListeners()
    }
    
    private fun setupListeners() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            
            if (validateInput(email, password)) {
                performLogin(email, password)
            }
        }
        
        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
    
    private fun validateInput(email: String, password: String): Boolean {
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
    
    private fun performLogin(email: String, password: String) {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnLogin.isEnabled = false
        
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.login(
                    LoginRequest(email, password)
                )
                
                binding.progressBar.visibility = View.GONE
                binding.btnLogin.isEnabled = true
                
                if (response.isSuccessful && response.body()?.success == true) {
                    val user = response.body()?.data
                    if (user != null) {
                        sessionManager.saveUserSession(user)
                        Toast.makeText(
                            this@LoginActivity,
                            "Bienvenido ${user.nombre}",
                            Toast.LENGTH_SHORT
                        ).show()
                        startMainActivity()
                    }
                } else {
                    val message = response.body()?.message ?: "Error al iniciar sesión"
                    Toast.makeText(this@LoginActivity, message, Toast.LENGTH_LONG).show()
                }
                
            } catch (e: Exception) {
                binding.progressBar.visibility = View.GONE
                binding.btnLogin.isEnabled = true
                Toast.makeText(
                    this@LoginActivity,
                    "Error de conexión: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
    
    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
