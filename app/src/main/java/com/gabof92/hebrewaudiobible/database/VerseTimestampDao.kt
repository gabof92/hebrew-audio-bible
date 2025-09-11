package com.gabof92.hebrewaudiobible.database

import androidx.room.Dao
import androidx.room.Query

@Dao
interface VerseTimestampDao {
    @Query("SELECT * FROM verse_timestamps WHERE id = :id")
    suspend fun getTimestampById (id: Int): VerseTimestamp

    @Query("SELECT * FROM verse_timestamps " +
            "WHERE BOOK=:book AND CHAPTER=:chapter " +
            "ORDER BY milliseconds ASC")
    suspend fun getChapterTimestamps(book: Int, chapter: Int): List<VerseTimestamp>
}