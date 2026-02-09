package com.example.mentall.ui.splash

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.example.mentall.MainActivity
import com.example.mentall.R
import com.example.mentall.data.prefs.SessionManager
import com.example.mentall.ui.auth.LoginActivity

/**
 * SplashActivity con video de inicio
 * Requisito TFG: Pantalla de splash con video .mp4
 */
class SplashActivity : AppCompatActivity() {

    private lateinit var videoView: VideoView
    private lateinit var sessionManager: SessionManager
    private var videoEnded = false

    companion object {
        private const val SPLASH_DISPLAY_LENGTH = 3000L // 3 segundos mínimo
        private const val VIDEO_NAME = "splash_mentall" // sin extensión
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        sessionManager = SessionManager(this)
        videoView = findViewById(R.id.videoView)

        setupVideo()
    }

    private fun setupVideo() {
        // Cargar video desde res/raw
        val videoUri = Uri.parse("android.resource://$packageName/${R.raw.splash_mentall}")
        videoView.setVideoURI(videoUri)

        // Configurar listeners
        videoView.setOnPreparedListener { mediaPlayer ->
            // Hacer que el video se reproduzca en loop durante el tiempo mínimo
            mediaPlayer.isLooping = false
            videoView.start()
        }

        videoView.setOnCompletionListener {
            videoEnded = true
            navigateToNextScreen()
        }

        videoView.setOnErrorListener { _, what, extra ->
            // Si hay error con el video, navegar directamente
            navigateToNextScreen()
            true
        }

        // Timeout de seguridad - navegar después del tiempo mínimo aunque el video no haya terminado
        Handler(Looper.getMainLooper()).postDelayed({
            if (!videoEnded) {
                navigateToNextScreen()
            }
        }, SPLASH_DISPLAY_LENGTH)
    }

    private fun navigateToNextScreen() {
        val intent = if (sessionManager.isLoggedIn()) {
            Intent(this, MainActivity::class.java)
        } else {
            Intent(this, LoginActivity::class.java)
        }
        
        startActivity(intent)
        finish()
        
        // Animación de transición suave
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    override fun onPause() {
        super.onPause()
        if (videoView.isPlaying) {
            videoView.pause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        videoView.stopPlayback()
    }
}
