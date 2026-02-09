package com.example.mentall

sealed class Route(val path: String) {
    data object Login : Route("login")
    data object Register : Route("register")
    data object Home : Route("home")
    data object EmotionEntry : Route("emotion_entry")      // emoción + actividad + foto
    data object Activities : Route("activities")          // cards scroll
    data object Sos : Route("sos")                        // llamada ya / programada
    data object Profile : Route("profile")
    data object Skills : Route("skills")                  // compartir habilidades
}
