package com.gabof92.hebrewaudiobible.presentation

import android.media.MediaPlayer
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AudioManager() {

    private var audioUrl = ""
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