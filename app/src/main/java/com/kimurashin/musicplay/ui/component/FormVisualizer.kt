package com.kimurashin.musicplay.ui.component

import android.content.Context
import android.media.AudioManager
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun VolumeControl() {
    val context = LocalContext.current
    val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    // Track the current volume level
    var currentVolume by remember { mutableStateOf(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)) }
    val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)

    // To continuously update volume, you can use a LaunchedEffect loop with a delay
    LaunchedEffect(Unit) {
        while (true) {
            currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
            kotlinx.coroutines.delay(500) // Polling every 500ms for volume changes
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = "Volume: $currentVolume / $maxVolume")
        LinearProgressIndicator(progress = currentVolume / maxVolume.toFloat())
    }
}

@Composable
fun WaveformVisualizer(
    waveform: ByteArray,
    colorList: List<Color>
) {
    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)) {
        if (waveform.isEmpty()) return@Canvas

        val width = size.width
        val height = size.height
        val barWidth = 6.dp.toPx()
        val spacing = 1.dp.toPx()
        val totalBarWidth = barWidth + spacing

        val gradientBrush = Brush.verticalGradient(colorList)

        for (i in waveform.indices) {
            val left = totalBarWidth * i
            val top = ((waveform[i] + 128).toFloat() * (height / 2) / 128)
            val right = left + barWidth
            val bottom = height

            drawRect(
                brush = gradientBrush,
                topLeft = androidx.compose.ui.geometry.Offset(left, top),
                size = androidx.compose.ui.geometry.Size(barWidth, bottom - top)
            )
        }
    }
}
