package com.gabof92.hebrewaudiobible.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.gabof92.hebrewaudiobible.data.database.entity.VerseTimestampEntity

@Dao
interface VerseTimestampDao {
    @Query("SELECT * FROM verse_timestamps WHERE id = :id")
    suspend fun getTimestampById (id: Int): VerseTimestampEntity

    @Query("SELECT * FROM verse_timestamps " +
            "WHERE BOOK=:book AND CHAPTER=:chapter " +
            "ORDER BY milliseconds ASC")
    suspend fun getChapterTimestamps(book: Int, chapter: Int): List<VerseTimestampEntity>
}