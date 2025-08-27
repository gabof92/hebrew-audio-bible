package com.gabof92.hebrewaudiobible

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.gabof92.hebrewaudiobible.data.BibleRepository
import com.gabof92.hebrewaudiobible.database.BibleRoomDataSource
import com.gabof92.hebrewaudiobible.network.BollsBibleDataSource
import com.gabof92.hebrewaudiobible.ui.screens.MainScreen
import com.gabof92.hebrewaudiobible.ui.screens.VerseDetailScreen
import com.gabof92.hebrewaudiobible.ui.screens.mainScreenDestination
import com.gabof92.hebrewaudiobible.ui.screens.verseDetailScreenDestination
import com.gabof92.hebrewaudiobible.ui.theme.HebrewAudioBibleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = (application as App).database
        val remoteDataSource = BollsBibleDataSource()
        val localDataSource = BibleRoomDataSource(
            db.originalWordDao(),
            db.audioSourceDao(),
            db.verseTimestampDao(),
            db.booksDao()
        )
        val repository = BibleRepository(localDataSource, remoteDataSource)

        enableEdgeToEdge()
        setContent {
            HebrewAudioBibleTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = MainScreen) {
                    mainScreenDestination(
                        repository = repository,
                        onNavigateToVerseDetail = { book, chapter, verse ->
                            navController.navigate(VerseDetailScreen(book, chapter, verse))
                        },
                    )
                    verseDetailScreenDestination(
                        repository = repository,
                    )
                }
            }
        }
    }
}