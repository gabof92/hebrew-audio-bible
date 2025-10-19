package com.gabof92.hebrewaudiobible.data

import com.gabof92.hebrewaudiobible.domain.RootWord
import com.gabof92.hebrewaudiobible.domain.WordPair

class BibleRepository(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) {
    suspend fun getVerseWords(book: Int, chapter: Int, verse: Int) =
        localDataSource.getVerseWords(book, chapter, verse)

    suspend fun getBook(bookNumber: Int) =
        localDataSource.getBook(bookNumber)

    suspend fun getVerses(book: Int, chapter: Int) =
        localDataSource.getVerses(book, chapter)

    suspend fun getChapterAudioUrl(book: Int, chapter: Int) =
        localDataSource.getChapterAudioUrl(book, chapter)

    suspend fun getChapterTimestamps(book: Int, chapter: Int) =
        localDataSource.getChapterTimestamps(book, chapter)

    suspend fun getWordDefinition(strongsNumber: Int) =
        remoteDataSource.getWordDefinition(strongsNumber)

    private suspend fun getRootWord(strongs: Int): RootWord? {
        return localDataSource.getRootWord(strongs)
    }

    private suspend fun insertRootWord(rootWord: RootWord) {
        localDataSource.insertRootWord(rootWord)
    }

    suspend fun getWordPairs(book: Int, chapter: Int, verse: Int): List<WordPair> {
        val originalWords = getVerseWords(book, chapter, verse)
        val wordPairs = mutableListOf<WordPair>()
        originalWords.forEach { word ->
            var rootWord = getRootWord(word.strongsHeb)
            if (rootWord == null) {
                rootWord = getWordDefinition(word.strongsHeb)
                insertRootWord(rootWord)
            }
            wordPairs.add(WordPair(word, rootWord))
        }
        return wordPairs
    }
}