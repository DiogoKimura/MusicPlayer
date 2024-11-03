package com.kimurashin.musicplay.ui.screen

import android.Manifest
import android.content.Context
import android.graphics.Color
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kimurashin.musicplay.data.getAudioFiles
import com.kimurashin.musicplay.data.toFormattedString
import com.kimurashin.musicplay.model.AudioInfo
import java.util.Date

@Composable
fun RequestPermission(permissionLauncher: ActivityResultLauncher<Array<String>>) {
    LaunchedEffect(Unit) {
        // Solicita ambas as permissões ao mesmo tempo
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_MEDIA_AUDIO
            )
        )
    }
}

@Composable
fun AudioFilesScreen(
    innerPadding: PaddingValues,
    context: Context,
    onExit: () -> Unit,
    onItemClick: (Pair<AudioInfo, Int>, List<AudioInfo>) -> Unit
) {
    // Store the list of audio URIs
    var audioList by remember { mutableStateOf(listOf<AudioInfo>()) }
    var loading by remember { mutableStateOf(true) }

    // Launcher to request runtime permission
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val recordAudioGranted = permissions[Manifest.permission.RECORD_AUDIO]
        val readMediaGranted = permissions[Manifest.permission.READ_MEDIA_AUDIO]

        if (recordAudioGranted == true && readMediaGranted == true) {
            loading = false
            audioList = getAudioFiles(context)
        } else {
            onExit()
            loading = false
        }
    }

    LoadingScreen(loading, "Carregando arquivos...")
    RequestPermission(permissionLauncher)
    AudioItemList(
        Modifier
            .fillMaxSize()
            .padding(innerPadding),
        audioList
    ) { audioInfo, audioInfos -> onItemClick(audioInfo, audioInfos) }
}

@Composable
fun AudioItemList(
    modifier: Modifier = Modifier,
    audioList: List<AudioInfo>,
    onItemClick: (Pair<AudioInfo, Int>, List<AudioInfo>) -> Unit
) {
    if (audioList.isEmpty()) {
        Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Nenhum arquivo encontrado/adicionado")
        }
    } else {
        LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            itemsIndexed(audioList) { index, audioInfo ->
                AudioItem(audioInfo) {
                    onItemClick.invoke(Pair(audioInfo, index), audioList)
                }
                Spacer(Modifier.height(8.dp))
                if (index < audioList.size - 1) HorizontalDivider()

            }
        }
    }
}

@Composable
fun AudioItem(audioInfo: AudioInfo, onItemClick: (AudioInfo) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick.invoke(audioInfo) },
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text("Nome: ${audioInfo.title}")
        Text("Artista: ${audioInfo.artist} - Coleção: ${audioInfo.album}")
        Text("Duração: ${audioInfo.duration} - Data: ${audioInfo.date?.toFormattedString()}")
    }
}

@Preview(backgroundColor = Color.WHITE.toLong(), showBackground = true)
@Composable
private fun AudioItemPreview() {
    AudioItem(
        audioInfo = AudioInfo(
            Uri.parse("content://uri"),
            "Título",
            "Artista",
            "Album",
            "00:01:23",
            Date()
        )
    ) {}
}