package com.example.mentall.utils

object Constants {
    
    // Mood Values
    const val MOOD_MUY_MAL = 1
    const val MOOD_MAL = 2
    const val MOOD_REGULAR = 3
    const val MOOD_BIEN = 4
    const val MOOD_GENIAL = 5
    
    // Mood Labels
    val MOOD_LABELS = mapOf(
        1 to "Muy mal",
        2 to "Mal",
        3 to "Regular",
        4 to "Bien",
        5 to "Genial"
    )
    
    // Mood Colors
    val MOOD_COLORS = mapOf(
        1 to "#FF4D4D",
        2 to "#FF944D",
        3 to "#FFD24D",
        4 to "#9BE15D",
        5 to "#00D4A6"
    )
    
    // Skill Levels
    val SKILL_LEVELS = listOf("Básico", "Intermedio", "Avanzado")
    
    // Days of Week
    val DIAS_SEMANA = mapOf(
        1 to "Lunes",
        2 to "Martes",
        3 to "Miércoles",
        4 to "Jueves",
        5 to "Viernes",
        6 to "Sábado",
        7 to "Domingo"
    )
    
    // Emergency Numbers
    const val TELEFONO_EMERGENCIAS = "112"
    const val TELEFONO_ESPERANZA = "717003717"
    
    // Request Codes
    const val REQUEST_CAMERA = 100
    const val REQUEST_GALLERY = 101
    
    // Shared Preferences Keys
    const val PREF_REMEMBER_CREDENTIALS = "remember_credentials"
    const val PREF_SAVED_EMAIL = "saved_email"
    const val PREF_SAVED_PASSWORD = "saved_password"
}
