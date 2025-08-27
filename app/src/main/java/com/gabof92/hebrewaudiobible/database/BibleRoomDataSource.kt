package com.gabof92.hebrewaudiobible.database

import com.gabof92.hebrewaudiobible.data.VerseText

class BibleRoomDataSource(
    val originalWordDao: OriginalWordDao,
    val audioSourceDao: AudioSourceDao,
    val verseTimestampDao: VerseTimestampDao,
    val booksDao: BooksDao
) {
    suspend fun getVerseWords(book: Int, chapter: Int, verse: Int) =
        originalWordDao.getOriginalVerse(book, chapter, verse)

    suspend fun getBook(bookNumber: Int) = booksDao.getBookByNumber(bookNumber)

    suspend fun getVerses(book: Int, chapter: Int): List<VerseText> =
        originalWordDao.getVerseTexts(book, chapter)
}