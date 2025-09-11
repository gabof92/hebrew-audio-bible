package com.gabof92.hebrewaudiobible.data

import com.gabof92.hebrewaudiobible.database.BibleRoomDataSource
import com.gabof92.hebrewaudiobible.network.BollsBibleDataSource

class BibleRepository(
    private val localDataSource: BibleRoomDataSource,
    private val remoteDataSource: BollsBibleDataSource
) {
    suspend fun getVerseWords(book: Int, chapter: Int, verse: Int) =
        localDataSource.getVerseWords(book, chapter, verse)

    suspend fun getBook(bookNumber: Int) =
        localDataSource.getBook(bookNumber)

    suspend fun getVerses(book: Int, chapter: Int) =
        localDataSource.getVerses(book, chapter)

    suspend fun getWordDefinitions(query: String) =
        remoteDataSource.getWordDefinitions(query)

    suspend fun getChapterAudioUrl(book: Int, chapter: Int) =
        localDataSource.getChapterAudioUrl(book, chapter)

    suspend fun getChapterTimestamps(book: Int, chapter: Int) =
        localDataSource.getChapterTimestamps(book, chapter)
}