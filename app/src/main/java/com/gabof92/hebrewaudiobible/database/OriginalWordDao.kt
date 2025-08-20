package com.gabof92.hebrewaudiobible.database

import androidx.room.Dao
import androidx.room.Query

@Dao
interface OriginalWordDao {
    @Query("SELECT * FROM original_words WHERE id = :id")
    suspend fun getOriginalWord(id: Int): OriginalWord

    @Query("SELECT * FROM original_words " +
            "WHERE BOOK=:book AND CHAPTER=:chapter " +
            "ORDER BY heb_sort ASC")
    suspend fun getOriginalChapter(book: Int, chapter: Int): List<OriginalWord>
}