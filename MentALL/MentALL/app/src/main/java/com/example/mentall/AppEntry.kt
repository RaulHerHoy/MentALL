package com.example.mentall

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable

@Composable
fun AppEntry() {
    var showVideo by rememberSaveable { mutableStateOf(true) }

    if (showVideo) {
        VideoSplash(
            onFinish = { showVideo = false }
        )
    } else {
        // ✅ Tu navegación normal (login/register/main con bottom bar fija)
        MentALLRoot()
    }
}
