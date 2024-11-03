package com.kimurashin.musicplay.ui.screen

import android.media.MediaPlayer
import android.media.audiofx.Visualizer
import android.net.Uri
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kimurashin.musicplay.data.getAudioInfo
import com.kimurashin.musicplay.data.toFormattedTime
import com.kimurashin.musicplay.model.AudioInfo
import com.kimurashin.musicplay.ui.component.ArtistSection
import com.kimurashin.musicplay.ui.component.SongMenu
import com.kimurashin.musicplay.ui.component.WaveformVisualizer
import com.kimurashin.musicplay.ui.theme.Gradient
import com.kimurashin.musicplay.ui.theme.MusicPlayTheme
import kotlinx.coroutines.delay
import kotlin.random.Random
import kotlin.random.nextInt

@Composable
fun MusicPlayScreen(
    innerPadding: PaddingValues,
    audio: String?,
    audioIndex: Int?,
    audioList: List<String>?,
    onMenuClick: () -> Unit
) {
    val context = LocalContext.current

    var isPlaying by remember { mutableStateOf(false) }
    var isShuffle by remember { mutableStateOf(false) }
    var slideDirection by remember { mutableIntStateOf(1) }
    var songIndex by remember { mutableIntStateOf(audioIndex ?: 0) }

    var mediaPlayerPosition by remember { mutableStateOf(0) }

    var volumeLevels by remember { mutableStateOf(byteArrayOf()) }

    var mediaPlayer by remember {
        mutableStateOf(
            MediaPlayer.create(context, Uri.parse(audio)).apply {
                setOnCompletionListener { isPlaying = false }
                setOnPreparedListener {
                    isPlaying = true
                    it.start()
                }
            }
        )
    }

    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            mediaPlayerPosition = mediaPlayer.currentPosition
            delay(50)
        }
    }

    DisposableEffect(isPlaying) {
        val visualizer = Visualizer(mediaPlayer.audioSessionId).apply {
            captureSize = Visualizer.getCaptureSizeRange()[1]
            setDataCaptureListener(object : Visualizer.OnDataCaptureListener {
                override fun onWaveFormDataCapture(
                    visualizer: Visualizer?,
                    waveform: ByteArray?,
                    samplingRate: Int
                ) {
                    waveform?.let {
                        volumeLevels = it
                    }
                }

                override fun onFftDataCapture(
                    visualizer: Visualizer?,
                    fft: ByteArray?,
                    samplingRate: Int
                ) {
                    // Not used in this example
                }
            }, Visualizer.getMaxCaptureRate() / 2, true, false)
            enabled = true
        }

        onDispose {
            visualizer.release()
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        color = Color.Black,
        contentColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .background(brush = Brush.verticalGradient(Gradient.musicPlayerScreen))
                .padding(16.dp)
        ) {
            WaveformVisualizer(volumeLevels, Gradient.musicPlayerScreen.reversed())

            Spacer(modifier = Modifier.height(16.dp))

            AnimatedContent(
                targetState = songIndex,
                transitionSpec = {
                    slideInHorizontally(
                        initialOffsetX = { 1000 * slideDirection },
                        animationSpec = tween(500)
                    ) togetherWith slideOutHorizontally(
                        targetOffsetX = { -1000 * slideDirection },
                        animationSpec = tween(500)
                    )
                }, label = ""
            ) { index ->
                ArtistSection(
                    getAudioInfo(context, Uri.parse(audioList?.get(index))) ?: AudioInfo.EMPTY
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            SongMenu(
                isPlaying = isPlaying,
                isShuffle = isShuffle,
                onPreviousSong = {
                    slideDirection = -1
                    songIndex = songIndex.switchSong(false, false, audioList?.size ?: 0)
                    mediaPlayer?.let {
                        isPlaying = false
                        mediaPlayerPosition = 0
                        it.stop()
                        it.reset()
                        it.release()
                    }

                    mediaPlayer =
                        MediaPlayer.create(context, Uri.parse(audioList?.get(songIndex))).apply {
                            setOnCompletionListener { isPlaying = false }
                            setOnPreparedListener {
                                isPlaying = true
                                it.start()
                            }
                        }

                },
                onForwardSong = {
                    slideDirection = 1
                    songIndex = songIndex.switchSong(true, isShuffle, audioList?.size ?: 0)
                    mediaPlayer?.let {
                        isPlaying = false
                        mediaPlayerPosition = 0
                        it.stop()
                        it.reset()
                        it.release()
                    }

                    mediaPlayer =
                        MediaPlayer.create(context, Uri.parse(audioList?.get(songIndex))).apply {
                            setOnCompletionListener { isPlaying = false }
                            setOnPreparedListener {
                                isPlaying = true
                                it.start()
                            }
                        }
                },
                onPlayPauseSong = {
                    isPlaying = !isPlaying

                    if (isPlaying) mediaPlayer.start() else mediaPlayer.pause()
                },
                onShuffle = {
                    isShuffle = !isShuffle
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(modifier = Modifier.fillMaxWidth(), text = "${songIndex + 1} / ${audioList?.size}")

            Spacer(modifier = Modifier.height(16.dp))

            LinearProgressIndicator(
                progress = { mediaPlayerPosition / mediaPlayer.duration.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "${
                    mediaPlayerPosition.toLong().toFormattedTime()
                } / ${mediaPlayer.duration.toLong().toFormattedTime()}"
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    DisposableEffect(Unit) {
        onDispose { mediaPlayer.release() }
    }
}

@Preview(showBackground = true)
@Composable
fun MusicPlayScreenPreview() {
    MusicPlayTheme {
        MusicPlayScreen(
            innerPadding = PaddingValues(0.dp),
            audio = null,
            audioIndex = 0,
            audioList = listOf("song1.mp3", "song2.mp3"),
            onMenuClick = {}
        )
    }
}

private fun Int.switchSong(forward: Boolean, shuffle: Boolean = false, size: Int): Int {
    if (shuffle) return Random.nextInt(0 until size)
    val switch = if (forward) 1 else -1
    val newIndex = this + switch
    return if (newIndex < 0) size - 1 else if (newIndex >= size) 0 else newIndex
}