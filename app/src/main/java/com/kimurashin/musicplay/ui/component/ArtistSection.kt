package com.kimurashin.musicplay.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kimurashin.musicplay.model.AudioInfo
import com.kimurashin.musicplay.ui.theme.MusicPlayTheme

@Composable
fun ArtistSection(audio: AudioInfo) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = audio.title,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = audio.album,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ArtistSectionPreview() {
    MusicPlayTheme {
        ArtistSection(AudioInfo.EMPTY)
    }
}