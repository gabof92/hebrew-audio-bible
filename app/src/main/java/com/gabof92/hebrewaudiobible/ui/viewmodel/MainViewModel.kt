package com.gabof92.hebrewaudiobible.ui.viewmodel

import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.gabof92.hebrewaudiobible.data.BibleRepository
import com.gabof92.hebrewaudiobible.data.VerseText
import com.gabof92.hebrewaudiobible.database.Book
import com.gabof92.hebrewaudiobible.database.VerseTimestamp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: BibleRepository,
) : ViewModel() {
    // Private MutableStateFlow that will hold the current list of verses
    private val _verseList = MutableStateFlow<List<VerseText>>(emptyList())

    // Public immutable StateFlow that Composables can collect
    val verseList: StateFlow<List<VerseText>> = _verseList.asStateFlow()

    private val _currentBook = MutableStateFlow<Book>(
        Book(19, "Psalms", 150)
    )
    val currentBook: StateFlow<Book> = _currentBook.asStateFlow()

    private val _currentChapterNumber = MutableStateFlow(1) // Default to chapter 1
    val currentChapter: StateFlow<Int> = _currentChapterNumber.asStateFlow()

    private lateinit var audioManager: AudioManager

    private val _audioManagerFlow = MutableStateFlow<AudioManager?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val isAudioPlaying: StateFlow<Boolean> = _audioManagerFlow.flatMapLatest { audioManager ->
        audioManager?.isPlaying ?: MutableStateFlow(false) // Provide a default if null
    }.stateIn(viewModelScope, SharingStarted.Lazily, false)

    private val _timestamps = MutableStateFlow<List<VerseTimestamp>>(emptyList())
    val timestamps: StateFlow<List<VerseTimestamp>> = _timestamps.asStateFlow()

    private var timestampCheckJob: Job? = null

    private val _currentAudioVerse = MutableStateFlow(0) // Default to chapter 1
    val currentAudioVerse: StateFlow<Int> = _currentAudioVerse.asStateFlow()


    init {
        // Initial data load
        loadVerses(_currentBook.value.number, _currentChapterNumber.value)
    }

    fun loadVerses(bookNumber: Int, chapter: Int) {
        viewModelScope.launch {
            _currentChapterNumber.value = chapter

            //Book
            val book = repository.getBook(bookNumber)
            _currentBook.value = book

            //Verses
            val verses = repository.getVerses(bookNumber, chapter)
            _verseList.value = verses

            //Audio
            val newAudioUrl = repository.getChapterAudioUrl(bookNumber, chapter)
            if (::audioManager.isInitialized) {
                audioManager.updateAudioSource(newAudioUrl)
            } else {
                audioManager = AudioManager(newAudioUrl)
                audioManager.updateAudioSource()
                _audioManagerFlow.value = audioManager
            }

            _timestamps.value = repository.getChapterTimestamps(bookNumber, chapter)

            _currentAudioVerse.value = 0
            startTimestampCheck()
        }
    }

    fun playAudio() {
        if (::audioManager.isInitialized) {
            audioManager.playAudio()
        }
    }

    fun pauseAudio() {
        if (::audioManager.isInitialized) {
            audioManager.pauseAudio()
        }
    }

    fun seekAudio(timestamp: Int) {
        if (::audioManager.isInitialized) {
            audioManager.seekAudio(timestamp)
        }
    }

    private fun startTimestampCheck() {
        timestampCheckJob?.cancel()
        timestampCheckJob = viewModelScope.launch {
            while (true) {
                if (::audioManager.isInitialized && isAudioPlaying.value) {
                    val position = audioManager.getPosition()
                    run foreach@{ //label to break foreach instead of while
                        timestamps.value.reversed().forEach { timestamp ->
                            if (position >= timestamp.millis) {
                                _currentAudioVerse.value = timestamp.verse
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
        if (::audioManager.isInitialized) {
            audioManager.release()
        }
        _audioManagerFlow.value = null
        timestampCheckJob?.cancel()
    }

    fun seekAudioByVerse(verseNumber: Int) {
        val timestamp = timestamps.value.find { it.verse == verseNumber }?.millis ?: 0
        seekAudio(timestamp)
    }
}

class AudioManager(private var audioUrl: String) {

    private var mediaPlayer: MediaPlayer? = null
    private var isPreparing = false

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    fun updateAudioSource(newUrl: String = audioUrl) {
        audioUrl = newUrl
        isPreparing = false

        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer()
        } else {
            mediaPlayer?.reset()
        }
        _isPlaying.value = false

        mediaPlayer?.apply {
            try {
                setDataSource(audioUrl)
                setOnPreparedListener { mp ->
                    Log.d("AudioManager", "MediaPlayer Prepared for: $audioUrl")
                    isPreparing = false
                }
                setOnCompletionListener { mp ->
                    Log.d("AudioManager", "MediaPlayer Completion for: $audioUrl")
                    _isPlaying.value = false
                    mp.seekTo(0)
                    mp.pause()
                }
                setOnErrorListener { mp, what, extra ->
                    Log.e(
                        "AudioManager",
                        "MediaPlayer Error: what=$what, extra=$extra for URL: $audioUrl"
                    )
                    isPreparing = false
                    _isPlaying.value = false
                    mp.reset() // Reset on error to allow for a new attempt
                    true // Error handled
                }

                Log.d("AudioManager", "Preparing audio: $audioUrl")
                isPreparing = true
                prepareAsync()

            } catch (e: Exception) {
                Log.e(
                    "AudioManager",
                    "Error setting data source or preparing: ${e.message} for URL $audioUrl"
                )
                isPreparing = false
                _isPlaying.value = false
                mediaPlayer?.reset()
            }
        }
    }

    fun playAudio() {
        if (isPreparing) {
            Log.d("AudioManager", "Already preparing, play request ignored.")
            return
        }
        mediaPlayer?.let {
            if (!it.isPlaying) {
                it.start()
                _isPlaying.value = true
            }
        }
    }

    fun seekAudio(timestamp: Int) {
        mediaPlayer?.let {
            // Check if it's in a valid state to seek
            if (it.isPlaying || it.isLooping || (mediaPlayer?.duration ?: 0) > 0) {
                if (it.duration > timestamp) {
                    it.seekTo(timestamp)
                    if (!it.isPlaying) { // Start only if it was paused
                        it.start()
                        _isPlaying.value = true
                    }
                }
            }
        }
    }

    fun pauseAudio() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
            _isPlaying.value = false
        }
    }

    fun getPosition(): Int {
        return mediaPlayer?.currentPosition ?: 0
    }

    fun release() {
        isPreparing = false
        _isPlaying.value = false
        mediaPlayer?.release()
        mediaPlayer = null
        Log.d("AudioManager", "MediaPlayer released")
    }
}

class MainViewModelFactory(private val repository: BibleRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}