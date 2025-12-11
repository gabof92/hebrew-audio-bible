package com.gabof92.hebrewaudiobible.data

import com.gabof92.hebrewaudiobible.domain.Book
import com.gabof92.hebrewaudiobible.domain.OriginalWord
import com.gabof92.hebrewaudiobible.domain.RootWord
import com.gabof92.hebrewaudiobible.domain.VerseText
import com.gabof92.hebrewaudiobible.domain.VerseTimeStamp

interface LocalDataSource {
    suspend fun getBook(bookNumber: Int): Book
    suspend fun getAllBooks(): List<Book>
    suspend fun getChapterAudioUrl(book: Int, chapter: Int): String
    suspend fun getVerseWords(book: Int, chapter: Int, verse: Int): List<OriginalWord>
    suspend fun getVerses(book: Int, chapter: Int): List<VerseText>
    suspend fun getChapterTimestamps(book: Int, chapter: Int) : List<VerseTimeStamp>
    suspend fun getRootWord(strongs: Int): RootWord?
    suspend fun insertRootWord(rootWord: RootWord)
}
