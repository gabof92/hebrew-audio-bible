package com.gabof92.hebrewaudiobible.database

import androidx.room.Dao
import androidx.room.Query

@Dao
interface AudioSourceDao {
    @Query("SELECT * FROM audio_sources WHERE id = :id")
    suspend fun getAudioSourceById(id: Int): AudioSource

    @Query("SELECT * FROM audio_sources " +
            "WHERE BOOK=:book AND CHAPTER=:chapter")
    suspend fun getChapterAudio(book: Int, chapter: Int): AudioSource

    @Query("SELECT url FROM audio_sources " +
            "WHERE BOOK=:book AND CHAPTER=:chapter")
    suspend fun getChapterAudioUrl(book: Int, chapter: Int): String
}