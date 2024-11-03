package com.kimurashin.musicplay.ui.route

import kotlinx.serialization.Serializable

sealed class MainRoute {
    @Serializable
    object Main

    @Serializable
    data class MusicPlayer(val audio: String, val audioIndex: Int, val uriList: String)

    @Serializable
    object PlaylistScreen
}
