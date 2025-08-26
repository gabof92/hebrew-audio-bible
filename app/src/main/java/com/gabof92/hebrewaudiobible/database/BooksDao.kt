package com.gabof92.hebrewaudiobible.database

import androidx.room.Dao
import androidx.room.Query

@Dao
interface BooksDao {
    @Query("SELECT * FROM books WHERE book_number = :bookNumber")
    suspend fun getBookByNumber(bookNumber: Int): Book
}