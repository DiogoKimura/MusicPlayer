package com.kimurashin.musicplay.data

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import com.kimurashin.musicplay.model.AudioInfo
import java.util.Locale

fun getAudioFiles(context: Context): List<AudioInfo> {
    val contentResolver: ContentResolver = context.contentResolver
    val audioList = mutableListOf<AudioInfo>()

    // Columns to retrieve
    val projection = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.ALBUM,
        MediaStore.Audio.Media.DURATION,
        MediaStore.Audio.Media.DATE_ADDED
    )

    // Filtering for music and recording
    val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"

    // Resolving query
    val query = contentResolver.query(
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
        projection,
        selection,
        null,
        null
    )

    query?.use { cursor ->
        // Get column indices
        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
        val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
        val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
        val albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
        val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
        val dateColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED)

        // Iterate through the query results and build the list
        while (cursor.moveToNext()) {
            val id = cursor.getLong(idColumn)
            val title = cursor.getStringOrNull(titleColumn)
            val artist = cursor.getStringOrNull(artistColumn)
            val album = cursor.getStringOrNull(albumColumn)
            val duration = cursor.getLongOrNull(durationColumn)
            val date = cursor.getStringOrNull(dateColumn)

            // Construct the content URI for the audio file
            val contentUri = Uri.withAppendedPath(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                id.toString()
            )

            audioList.add(
                AudioInfo(
                    uri = contentUri,
                    title = title ?: "Sem título",
                    artist = artist ?: "Sem artista",
                    album = album ?: "Sem album ou coleção",
                    duration = duration?.toFormattedTime() ?: "Sem duração",
                    date = title?.getDateOnTitle()
                )
            )
        }
    }

    return audioList.filterNot { it.album.lowercase(Locale.getDefault()).contains("whatsapp") }
        .sortedBy { it.date }
}