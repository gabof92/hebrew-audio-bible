package com.gabof92.hebrewaudiobible.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.gabof92.hebrewaudiobible.data.database.entity.AudioSourceEntity

@Dao
interface AudioSourceDao {
    @Query("SELECT * FROM audio_sources WHERE id = :id")
    suspend fun getAudioSourceById(id: Int): AudioSourceEntity

    @Query("SELECT * FROM audio_sources " +
            "WHERE BOOK=:book AND CHAPTER=:chapter")
    suspend fun getChapterAudio(book: Int, chapter: Int): AudioSourceEntity

    @Query("SELECT url FROM audio_sources " +
            "WHERE BOOK=:book AND CHAPTER=:chapter")
    suspend fun getChapterAudioUrl(book: Int, chapter: Int): String
}