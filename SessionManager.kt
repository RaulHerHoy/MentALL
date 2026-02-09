package com.example.mentall.data.prefs

import android.content.Context
import android.content.SharedPreferences
import com.example.mentall.data.models.User

class SessionManager(context: Context) {
    
    private val prefs: SharedPreferences = 
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    
    companion object {
        private const val PREF_NAME = "MentALLPrefs"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_DARK_THEME = "dark_theme"
    }
    
    // ========== User Session ==========
    fun saveUserSession(user: User) {
        prefs.edit().apply {
            putInt(KEY_USER_ID, user.idUsuario)
            putString(KEY_USER_NAME, user.nombre)
            putString(KEY_USER_EMAIL, user.email)
            putBoolean(KEY_IS_LOGGED_IN, true)
            apply()
        }
    }
    
    fun clearSession() {
        prefs.edit().apply {
            remove(KEY_USER_ID)
            remove(KEY_USER_NAME)
            remove(KEY_USER_EMAIL)
            putBoolean(KEY_IS_LOGGED_IN, false)
            apply()
        }
    }
    
    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    
    fun getUserId(): Int = prefs.getInt(KEY_USER_ID, -1)
    
    fun getUserName(): String = prefs.getString(KEY_USER_NAME, "") ?: ""
    
    fun getUserEmail(): String = prefs.getString(KEY_USER_EMAIL, "") ?: ""
    
    fun getUser(): User? {
        val userId = getUserId()
        if (userId == -1) return null
        
        return User(
            idUsuario = userId,
            nombre = getUserName(),
            email = getUserEmail()
        )
    }
    
    // ========== Theme ==========
    fun setDarkTheme(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_DARK_THEME, enabled).apply()
    }
    
    fun isDarkThemeEnabled(): Boolean = prefs.getBoolean(KEY_DARK_THEME, false)
}
