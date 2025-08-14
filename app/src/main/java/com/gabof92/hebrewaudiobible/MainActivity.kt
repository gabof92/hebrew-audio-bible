package com.gabof92.hebrewaudiobible

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.gabof92.hebrewaudiobible.ui.screens.MainScreen
import com.gabof92.hebrewaudiobible.ui.screens.VerseDetailScreen
import com.gabof92.hebrewaudiobible.ui.screens.mainScreenDestination
import com.gabof92.hebrewaudiobible.ui.screens.verseDetailScreenDestination
import com.gabof92.hebrewaudiobible.ui.theme.HebrewAudioBibleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HebrewAudioBibleTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = MainScreen) {
                    mainScreenDestination(
                        onNavigateToVerseDetail = { book, chapter, verse ->
                            navController.navigate(VerseDetailScreen(book, chapter, verse))
                        },
                    )
                    verseDetailScreenDestination()
                }
            }
        }
    }
}