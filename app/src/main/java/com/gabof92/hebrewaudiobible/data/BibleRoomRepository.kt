package com.gabof92.hebrewaudiobible.data

import com.gabof92.hebrewaudiobible.database.AudioSourceDao
import com.gabof92.hebrewaudiobible.database.BooksDao
import com.gabof92.hebrewaudiobible.database.OriginalWordDao
import com.gabof92.hebrewaudiobible.database.VerseTimestampDao

class BibleRoomRepository(
    val originalWordDao: OriginalWordDao,
    val audioSourceDao: AudioSourceDao,
    val verseTimestampDao: VerseTimestampDao,
    val booksDao: BooksDao
) {

    suspend fun getBook(bookNumber: Int) = booksDao.getBookByNumber(bookNumber)

    suspend fun getVerses(book: Int, chapter: Int): List<VerseText> =
        originalWordDao.getVerseTexts(book, chapter)

}