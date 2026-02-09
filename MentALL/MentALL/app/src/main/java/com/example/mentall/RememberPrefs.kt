package com.example.mentall

import android.content.Context

object RememberPrefs {
    private const val PREFS = "mentall_prefs"
    private const val KEY_REMEMBER = "remember"
    private const val KEY_EMAIL = "email"
    private const val KEY_PASS = "pass"
    private const val KEY_NAME = "name"

    fun load(context: Context): SavedCreds {
        val sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        return SavedCreds(
            remember = sp.getBoolean(KEY_REMEMBER, false),
            email = sp.getString(KEY_EMAIL, "") ?: "",
            pass = sp.getString(KEY_PASS, "") ?: "",
            name = sp.getString(KEY_NAME, "") ?: ""
        )
    }

    fun save(context: Context, remember: Boolean, email: String, pass: String, name: String? = null) {
        val sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        sp.edit().apply {
            putBoolean(KEY_REMEMBER, remember)
            if (remember) {
                putString(KEY_EMAIL, email)
                putString(KEY_PASS, pass)
                if (name != null) putString(KEY_NAME, name)
            } else {
                remove(KEY_EMAIL)
                remove(KEY_PASS)
                remove(KEY_NAME)
            }
            apply()
        }
    }
}

data class SavedCreds(
    val remember: Boolean,
    val email: String,
    val pass: String,
    val name: String
)
