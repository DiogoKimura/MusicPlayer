package com.kimurashin.musicplay.model

import android.net.Uri
import android.provider.MediaStore.Audio
import java.text.SimpleDateFormat
import java.util.Date

data class AudioInfo(
    val uri: Uri,
    val title: String = "Sem título",
    val artist: String = "Sem artista",
    val album: String = "Sem album",
    val duration: String = "Não encontrado",
    val date: Date? = null
) {
    companion object {
        val EMPTY = AudioInfo(Uri.parse(""))
    }
}
