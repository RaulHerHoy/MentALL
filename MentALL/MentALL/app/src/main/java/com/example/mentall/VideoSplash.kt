package com.example.mentall

import android.net.Uri
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.annotation.OptIn


@OptIn(UnstableApi::class)
@Composable
fun VideoSplash(
    onFinish: () -> Unit
) {
    val context = LocalContext.current
    var finished by remember { mutableStateOf(false) }

    val videoUri = remember {
        Uri.parse("android.resource://${context.packageName}/${R.raw.splashmentall}")
    }

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUri))
            prepare()
            playWhenReady = true
        }
    }

    DisposableEffect(Unit) {
        val listener = object : androidx.media3.common.Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == androidx.media3.common.Player.STATE_ENDED && !finished) {
                    finished = true
                    onFinish()
                }
            }
        }
        exoPlayer.addListener(listener)

        onDispose {
            exoPlayer.removeListener(listener)
            exoPlayer.release()
        }
    }

    AndroidView(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures {
                    if (!finished) {
                        finished = true
                        onFinish()
                    }
                }
            },
        factory = {
            PlayerView(it).apply {
                player = exoPlayer
                useController = false

                // ✅ Pantalla completa tipo "cover"
                setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM)
            }
        }
    )
}
