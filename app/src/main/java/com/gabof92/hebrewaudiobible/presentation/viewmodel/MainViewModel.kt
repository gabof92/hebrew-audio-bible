package com.gabof92.hebrewaudiobible.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabof92.hebrewaudiobible.domain.Book
import com.gabof92.hebrewaudiobible.domain.VerseText
import com.gabof92.hebrewaudiobible.domain.VerseTimeStamp
import com.gabof92.hebrewaudiobible.presentation.AudioManager
import com.gabof92.hebrewaudiobible.usecases.GetAudioUrlByChapterUseCase
import com.gabof92.hebrewaudiobible.usecases.GetBookUseCase
import com.gabof92.hebrewaudiobible.usecases.GetTimestampsByChapterUseCase
import com.gabof92.hebrewaudiobible.usecases.GetVersesByChapterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MainUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val book: Book = Book(19, "Psalms", 150),
    val chapter: Int = 1,
    val verses: List<VerseText> = emptyList(),
    val timestamps: List<VerseTimeStamp> = emptyList(),
    val isAudioPlaying: Boolean = false,
    val currentAudioVerse: Int = 0,
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getBookUseCase: GetBookUseCase,
    private val getVersesUseCase: GetVersesByChapterUseCase,
    private val getTimestampsUseCase: GetTimestampsByChapterUseCase,
    private val getAudioUrlUseCase: GetAudioUrlByChapterUseCase,
    ) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private val audioManager = AudioManager()

    private var timestampCheckJob: Job? = null


    init {
        // update uiState when audioManager starts/stops playing
        viewModelScope.launch {
            audioManager.isPlaying.collect { playing ->
                _uiState.update { it.copy(isAudioPlaying = playing) }
            }
        }
        // Initial data load
        loadVerses(uiState.value.book.number, uiState.value.chapter)
    }

    fun loadVerses(bookNumber: Int = uiState.value.book.number,
                   chapter: Int
    ) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, error = null) }

                //loading data
                val book = getBookUseCase(bookNumber)
                val verses = getVersesUseCase(bookNumber, chapter)
                val timestamps = getTimestampsUseCase(bookNumber, chapter)
                val newAudioUrl = getAudioUrlUseCase(bookNumber, chapter)

                _uiState.update{
                    it.copy(
                        isLoading = false,
                        book = book,
                        chapter = chapter,
                        verses = verses,
                        timestamps = timestamps,
                        currentAudioVerse = 0,
                    )
                }

                audioManager.updateAudioSource(newAudioUrl)
                startTimestampCheck()
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Failed to load data") }
                Log.e("MainViewModel", "Error loading data", e)
            }
        }
    }

    fun togglePlay() {
        if (uiState.value.isAudioPlaying)
            audioManager.pauseAudio()
        else
            audioManager.playAudio()
    }

    fun seekAudio(timestamp: Int) = audioManager.seekAudio(timestamp)

    private fun startTimestampCheck() {
        timestampCheckJob?.cancel()
        timestampCheckJob = viewModelScope.launch {
            while (isActive) {
                if (uiState.value.isAudioPlaying) {
                    val position = audioManager.getPosition()
                    run foreach@{ //label to break foreach instead of while
                        uiState.value.timestamps.reversed().forEach { timestamp ->
                            if (position >= timestamp.millis) {
                                _uiState.update { it.copy(currentAudioVerse = timestamp.verse) }
                                return@foreach
                            }
                        }
                    }
                }
                delay(500)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioManager?.release()
        timestampCheckJob?.cancel()
    }

    fun seekAudioByVerse(verseNumber: Int) {
        val timestamp = uiState.value.timestamps.find { it.verse == verseNumber }?.millis ?: 0
        seekAudio(timestamp)
    }

}

