package com.kimurashin.musicplay.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.ShuffleOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SongMenu(
    isPlaying: Boolean,
    isShuffle: Boolean,
    onPreviousSong: () -> Unit,
    onForwardSong: () -> Unit,
    onPlayPauseSong: () -> Unit,
    onShuffle: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { onPreviousSong.invoke() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Previous"
                )
            }

            IconButton(onClick = { onPlayPauseSong.invoke() }) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = "Play"
                )
            }

            IconButton(onClick = { onForwardSong.invoke() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Forward"
                )
            }

            IconButton(onClick = { onShuffle.invoke() }) {
                Icon(
                    imageVector = if (isShuffle) Icons.Default.ShuffleOn else Icons.Default.Shuffle,
                    contentDescription = "Forward"
                )
            }
        }
    }
}

@Preview
@Composable
private fun SongMenuPreview() {
    SongMenu(
        true,
        true,
        {}, {}, {}, {}
    )
}