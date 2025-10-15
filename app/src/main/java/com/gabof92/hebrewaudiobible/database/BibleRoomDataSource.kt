package com.gabof92.hebrewaudiobible.database

import com.gabof92.hebrewaudiobible.data.VerseText

class BibleRoomDataSource(
    val originalWordDao: OriginalWordDao,
    val audioSourceDao: AudioSourceDao,
    val verseTimestampDao: VerseTimestampDao,
    val booksDao: BooksDao,
    val rootWordDao: RootWordDao,
) {
    suspend fun getVerseWords(book: Int, chapter: Int, verse: Int) =
        originalWordDao.getOriginalVerse(book, chapter, verse)

    suspend fun getBook(bookNumber: Int) =
        booksDao.getBookByNumber(bookNumber)

    suspend fun getVerses(book: Int, chapter: Int): List<VerseText> =
        originalWordDao.getVerseTexts(book, chapter)

    suspend fun getChapterAudioUrl(book: Int, chapter: Int) =
        audioSourceDao.getChapterAudioUrl(book, chapter)

    suspend fun getChapterTimestamps(book: Int, chapter: Int) =
        verseTimestampDao.getChapterTimestamps(book, chapter)

    suspend fun getRootWord(language: String, strongs: Int) =
        rootWordDao.getRootWord(language, strongs)

    suspend fun insertRootWord(rootWord: RootWord) =
        rootWordDao.insert(rootWord)

}