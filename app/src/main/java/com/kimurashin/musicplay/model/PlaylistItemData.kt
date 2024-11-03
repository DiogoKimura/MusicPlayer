package com.kimurashin.musicplay.model

data class PlaylistItemData(
    val name: String,
    val rating: Float,
    val listenerCount: Float
)

val playList = listOf(
    PlaylistItemData(
        name = "Chill Vibes",
        rating = 4.5f,
        listenerCount = 125000f
    ), PlaylistItemData(
        name = "Top Hits 2024",
        rating = 4.8f,
        listenerCount = 5000000f
    ), PlaylistItemData(
        name = "Indie Essentials",
        rating = 4.3f,
        listenerCount = 78000f
    ), PlaylistItemData(
        name = "Morning Acoustic",
        rating = 4.6f,
        listenerCount = 220000f
    ), PlaylistItemData(
        name = "Lo-Fi Beats",
        rating = 4.9f,
        listenerCount = 3400000f
    ), PlaylistItemData(
        name = "Pop Classics",
        rating = 4.7f,
        listenerCount = 1500000f
    ), PlaylistItemData(
        name = "Rock Legends",
        rating = 4.4f,
        listenerCount = 480000f
    ), PlaylistItemData(
        name = "Electronic Night",
        rating = 4.2f,
        listenerCount = 89000f
    ), PlaylistItemData(
        name = "Jazz & Blues",
        rating = 4.5f,
        listenerCount = 72000f
    ), PlaylistItemData(
        name = "Reggae Vibes",
        rating = 4.3f,
        listenerCount = 65000f
    )
)
