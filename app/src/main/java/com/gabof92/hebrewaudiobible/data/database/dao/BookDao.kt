package com.gabof92.hebrewaudiobible.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.gabof92.hebrewaudiobible.data.database.entity.BookEntity

@Dao
interface BookDao {
    @Query("SELECT * FROM books WHERE book_number = :bookNumber")
    suspend fun getBookByNumber(bookNumber: Int): BookEntity

    @Query("SELECT * FROM books")
    suspend fun getAllBooks(): List<BookEntity>
}