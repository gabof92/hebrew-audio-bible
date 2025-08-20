package com.gabof92.hebrewaudiobible.database

import androidx.room.Dao
import androidx.room.Query

@Dao
interface VerseTimestampDao {
    @Query("SELECT * FROM verse_timestamp WHERE id = :id")
    suspend fun getTimestampById (id: Int): VerseTimestamp

    @Query("SELECT * FROM verse_timestamp " +
            "WHERE audio_source=:audioSource " +
            "ORDER BY milliseconds ASC")
    suspend fun getChapterTimestamps(audioSource: Int): List<VerseTimestamp>
}