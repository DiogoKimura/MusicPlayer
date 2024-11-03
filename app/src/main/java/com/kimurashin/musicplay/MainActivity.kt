package com.kimurashin.musicplay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kimurashin.musicplay.ui.route.MusicAppRoute
import com.kimurashin.musicplay.ui.theme.MusicPlayTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MusicPlayTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    text = "Music Play"
                                )
                            },
                            navigationIcon = {
                                Icon(
                                    modifier = Modifier.clickable { finish() },
                                    imageVector = Icons.Default.Close,
                                    contentDescription = ""
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                ) { innerPadding ->
                    MusicAppRoute(innerPadding) {
                        finish()
                    }
                }
            }
        }
    }
}