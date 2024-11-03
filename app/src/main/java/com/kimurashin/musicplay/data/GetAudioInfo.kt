package com.kimurashin.musicplay.data

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import com.kimurashin.musicplay.model.AudioInfo
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.regex.Pattern

fun getAudioInfo(context: Context, audioUri: Uri): AudioInfo? {
    val contentResolver: ContentResolver = context.contentResolver

    // Define the columns to retrieve
    val projection = arrayOf(
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.ALBUM,
        MediaStore.Audio.Media.DURATION,
        MediaStore.Audio.Media.DATE_ADDED
    )

    // Query the content resolver
    val cursor: Cursor? = contentResolver.query(
        audioUri, projection, null, null, null
    )

    cursor?.use {
        if (it.moveToFirst()) {
            // Get column indices
            val titleIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val durationIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val dateColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED)

            // Extract data from the cursor
            val title = it.getStringOrNull(titleIndex)
            val artist = it.getStringOrNull(artistIndex)
            val album = it.getStringOrNull(albumIndex)
            val duration = it.getLongOrNull(durationIndex)
            val date = it.getStringOrNull(dateColumn)

            // Return the audio metadata object
            return AudioInfo(
                uri = audioUri,
                title = title ?: "Sem título",
                artist = artist ?: "Sem artista",
                album = album ?: "Sem album ou coleção",
                duration = duration?.toFormattedTime() ?: "Sem duração",
                date = title?.getDateOnTitle()
            )
        }
    }
    return null // Return null if metadata not found
}

fun Long.toFormattedTime(): String {
    val hours = this / 3600000
    val minutes = (this % 3600000) / 60000
    val seconds = (this % 60000) / 1000
    return "%02d:%02d:%02d".format(hours, minutes, seconds)
}

fun String.toDate(): Date? {
    val inputFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
    val date: Date = inputFormat.parse(this) ?: return null
    return date
    //val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
}

fun Date.toFormattedString(): String {
    return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(this)
}

fun String.getDateOnTitle(): Date? {
    val regex = "-(\\d{8})-"

    val pattern = Pattern.compile(regex)
    val matcher = pattern.matcher(this)

    if (matcher.find()) {
        val dateStr = matcher.group()
        return dateStr.replace("-", "").toDate()
    } else {
        return null
    }
}