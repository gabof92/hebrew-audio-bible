package com.gabof92.hebrewaudiobible.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.gabof92.hebrewaudiobible.App
import com.gabof92.hebrewaudiobible.data.BibleRepository
import com.gabof92.hebrewaudiobible.data.database.BibleRoomDataSource
import com.gabof92.hebrewaudiobible.data.network.BollsBibleDataSource
import com.gabof92.hebrewaudiobible.presentation.viewmodel.MainViewModelFactory
import com.gabof92.hebrewaudiobible.ui.screens.MainScreen
import com.gabof92.hebrewaudiobible.ui.screens.VerseDetailScreen
import com.gabof92.hebrewaudiobible.ui.screens.mainScreenDestination
import com.gabof92.hebrewaudiobible.ui.screens.verseDetailScreenDestination
import com.gabof92.hebrewaudiobible.ui.theme.HebrewAudioBibleTheme
import com.gabof92.hebrewaudiobible.usecases.GetAudioUrlByChapterUseCase
import com.gabof92.hebrewaudiobible.usecases.GetBookUseCase
import com.gabof92.hebrewaudiobible.usecases.GetTimestampsByChapterUseCase
import com.gabof92.hebrewaudiobible.usecases.GetVersesByChapterUseCase
import com.gabof92.hebrewaudiobible.usecases.GetWordPairsUseCase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = (application as App).database
        val remoteDataSource = BollsBibleDataSource()
        val localDataSource = BibleRoomDataSource(
            db.originalWordDao(),
            db.audioSourceDao(),
            db.verseTimestampDao(),
            db.booksDao(),
            db.rootWordDao(),
        )
        val repository = BibleRepository(localDataSource, remoteDataSource)

        val audioUrlUseCase = GetAudioUrlByChapterUseCase(repository)
        val bookUseCase = GetBookUseCase(repository)
        val versesUseCase = GetVersesByChapterUseCase(repository)
        val timestampsUseCase = GetTimestampsByChapterUseCase(repository)
        val wordPairsUseCase = GetWordPairsUseCase(repository)

        val mainViewModelFactory = MainViewModelFactory(
            bookUseCase,
            versesUseCase,
            timestampsUseCase,
            audioUrlUseCase,
        )

        enableEdgeToEdge()
        setContent {
            HebrewAudioBibleTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = MainScreen) {
                    mainScreenDestination(
                        mainViewModelFactory,
                        onNavigateToVerseDetail = { book, chapter, verse ->
                            navController.navigate(VerseDetailScreen(book, chapter, verse))
                        },
                    )
                    verseDetailScreenDestination(
                        bookUseCase,
                        wordPairsUseCase,
                    )
                }
            }
        }
    }
}