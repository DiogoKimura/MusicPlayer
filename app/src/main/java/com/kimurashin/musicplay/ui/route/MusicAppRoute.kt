package com.kimurashin.musicplay.ui.route

import android.net.Uri
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.kimurashin.musicplay.lover
import com.kimurashin.musicplay.model.ArtistSong
import com.kimurashin.musicplay.ui.screen.AudioFilesScreen
import com.kimurashin.musicplay.ui.screen.MusicPlayScreen
import com.kimurashin.musicplay.ui.screen.PlayListScreen

@Composable
fun MusicAppRoute(
    innerPadding: PaddingValues,
    onExit: () -> Unit
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = MainRoute.Main) {
        composable<MainRoute.Main> {
            AudioFilesScreen(innerPadding, LocalContext.current, onExit = onExit) { audio, list ->
                navController.navigate(
                    MainRoute.MusicPlayer(
                        Uri.encode(audio.first.uri.toString()),
                        audio.second,
                        list.joinToString(",") { it.uri.toString() }
                    )
                )
            }
        }
        composable<MainRoute.PlaylistScreen> {
            PlayListScreen(
                onItemClick = {
                    navController.popBackStack()
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable<MainRoute.MusicPlayer> { backStackEntry ->
            val musicPlayer = backStackEntry.toRoute<MainRoute.MusicPlayer>()
            MusicPlayScreen(
                innerPadding,
                Uri.decode(musicPlayer.audio),
                musicPlayer.audioIndex,
                musicPlayer.uriList.split(",")
            ) {
                navController.popBackStack()
                navController.navigate(MainRoute.PlaylistScreen)
            }
        }
    }
}

val songList = listOf(
    ArtistSong(
        imageUri = "https://i.scdn.co/image/ab67616d0000b273e787cffec20aa2a396a61647",
        artistName = "Taylor Swift",
        title = "Lover",
        songLyrics = lover
    ),
    ArtistSong(
        imageUri = "https://upload.wikimedia.org/wikipedia/pt/9/9c/Ed_Sheeran_Perfect.jpg",
        artistName = "Ed Sheeran",
        title = "Perfect",
        songLyrics = lover
    ),
    ArtistSong(
        imageUri = "https://m.media-amazon.com/images/M/MV5BNTc4ODVkMmMtZWY3NS00OWI4LWE1YmYtN2NkNDA3ZjcyNTkxXkEyXkFqcGc@._V1_.jpg",
        artistName = "Adele",
        title = "Hello",
        songLyrics = lover
    ),
    ArtistSong(
        imageUri = "https://upload.wikimedia.org/wikipedia/pt/4/47/Yellow_cover.JPG",
        artistName = "Coldplay",
        title = "Yellow",
        songLyrics = lover
    )
)
