package com.example.mentall.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.mentall.R
import com.example.mentall.data.api.RetrofitClient
import com.example.mentall.data.local.DatabaseHelper
import com.example.mentall.data.models.UpdateNameRequest
import com.example.mentall.data.models.UpdatePasswordRequest
import com.example.mentall.data.prefs.SessionManager
import com.example.mentall.databinding.FragmentProfileBinding
import com.example.mentall.ui.auth.LoginActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import com.example.mentall.ui.contacts.EmergencyContactsActivity

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var sessionManager: SessionManager
    private lateinit var databaseHelper: DatabaseHelper

    private var userId: Int = -1
    private var userEmail: String = ""
    private var userName: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())
        databaseHelper = DatabaseHelper(requireContext())

        loadUserInfo()
        setupClickListeners()
    }

    override fun onResume() {
        super.onResume()
        // Refrescar contador al volver de la pantalla de contactos
        if (userId != -1) {
            val contactCount = databaseHelper.getContactCount(userId)
            binding.tvContactCount.text = "$contactCount contactos de emergencia"
        }
    }

    private fun loadUserInfo() {
        val user = sessionManager.getUser()

        if (user != null) {
            userId = user.idUsuario
            userName = user.nombre
            userEmail = user.email

            binding.tvUserName.text = userName
            binding.tvUserEmail.text = userEmail

            val isDarkTheme = sessionManager.isDarkThemeEnabled()
            binding.switchDarkTheme.isChecked = isDarkTheme

            val contactCount = databaseHelper.getContactCount(userId)
            binding.tvContactCount.text = "$contactCount contactos de emergencia"
        } else {
            navigateToLogin()
        }
    }

    private fun setupClickListeners() {
        // Cambiar nombre
        binding.btnChangeName.setOnClickListener { showChangeNameDialog() }

        // Cambiar contraseña
        binding.btnChangePassword.setOnClickListener { showChangePasswordDialog() }

        // Switch de tema
        binding.switchDarkTheme.setOnCheckedChangeListener { _, isChecked ->
            changeTheme(isChecked)
        }

        // Cerrar sesión
        binding.btnLogout.setOnClickListener { showLogoutConfirmation() }

        // ✅ NUEVO: Abrir gestión de contactos desde el perfil
        binding.tvContactCount.setOnClickListener {
            val intent = Intent(requireContext(), EmergencyContactsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showChangeNameDialog() {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_change_name, null)

        val etNewName = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(
            R.id.etNewName
        )

        etNewName.setText(userName)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Cambiar nombre")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                val newName = etNewName.text.toString().trim()
                if (newName.isNotEmpty() && newName != userName) updateUserName(newName)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun updateUserName(newName: String) {
        binding.progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val request = UpdateNameRequest(idUsuario = userId, nombre = newName)
                val response = RetrofitClient.apiService.updateUserName(request)

                if (response.isSuccessful && response.body()?.success == true) {
                    val updatedUser = response.body()?.data
                    if (updatedUser != null) {
                        sessionManager.saveUserSession(updatedUser)
                        userName = updatedUser.nombre
                        binding.tvUserName.text = userName

                        Toast.makeText(context, "Nombre actualizado correctamente", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(
                        context,
                        response.body()?.message ?: "Error al actualizar nombre",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error de conexión: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun showChangePasswordDialog() {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_change_password, null)

        val etCurrentPassword =
            dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etCurrentPassword)
        val etNewPassword =
            dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etNewPassword)
        val etConfirmPassword =
            dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etConfirmPassword)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Cambiar contraseña")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                val currentPassword = etCurrentPassword.text.toString()
                val newPassword = etNewPassword.text.toString()
                val confirmPassword = etConfirmPassword.text.toString()

                when {
                    currentPassword.isEmpty() -> Toast.makeText(context, "Introduce tu contraseña actual", Toast.LENGTH_SHORT).show()
                    newPassword.isEmpty() -> Toast.makeText(context, "Introduce la nueva contraseña", Toast.LENGTH_SHORT).show()
                    newPassword.length < 6 -> Toast.makeText(context, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
                    newPassword != confirmPassword -> Toast.makeText(context, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                    else -> updateUserPassword(currentPassword, newPassword)
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun updateUserPassword(currentPassword: String, newPassword: String) {
        binding.progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val request = UpdatePasswordRequest(
                    idUsuario = userId,
                    passwordActual = currentPassword,
                    passwordNueva = newPassword
                )

                val response = RetrofitClient.apiService.updateUserPassword(request)

                if (response.isSuccessful && response.body()?.success == true) {
                    Toast.makeText(context, "Contraseña actualizada correctamente", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        context,
                        response.body()?.message ?: "Error al actualizar contraseña",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error de conexión: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun changeTheme(isDarkTheme: Boolean) {
        sessionManager.setDarkTheme(isDarkTheme)
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkTheme) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )

        Toast.makeText(
            context,
            "Tema cambiado a ${if (isDarkTheme) "oscuro" else "claro"}",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showLogoutConfirmation() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Cerrar sesión")
            .setMessage("¿Estás seguro de que quieres cerrar sesión?")
            .setPositiveButton("Cerrar sesión") { _, _ -> performLogout() }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun performLogout() {
        sessionManager.clearSession()
        databaseHelper.deleteAllContactsForUser(userId)

        Toast.makeText(context, "Sesión cerrada correctamente", Toast.LENGTH_SHORT).show()
        navigateToLogin()
    }

    private fun navigateToLogin() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
