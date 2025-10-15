package com.gabof92.hebrewaudiobible.data

import com.gabof92.hebrewaudiobible.database.BibleRoomDataSource
import com.gabof92.hebrewaudiobible.database.RootWord
import com.gabof92.hebrewaudiobible.network.BollsBibleDataSource
import com.gabof92.hebrewaudiobible.network.RootWordResult

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

    suspend fun getChapterAudioUrl(book: Int, chapter: Int) =
        localDataSource.getChapterAudioUrl(book, chapter)

    suspend fun getChapterTimestamps(book: Int, chapter: Int) =
        localDataSource.getChapterTimestamps(book, chapter)

    suspend fun getWordDefinitions(query: String) =
        remoteDataSource.getWordDefinitions(query)

    private suspend fun getRootWord(language: String, strongs: Int): RootWord? {
        return localDataSource.getRootWord(language, strongs)
    }

    private suspend fun insertRootWord(rootWord: RootWord) {
        localDataSource.insertRootWord(rootWord)
    }

    suspend fun getWordPairs(book: Int, chapter: Int, verse: Int): List<WordPair> {
        val originalWords = getVerseWords(book, chapter, verse)
        val wordPairs = mutableListOf<WordPair>()
        originalWords.forEach { word ->
            var rootWord = getRootWord("H", word.strongsHeb)
            if (rootWord == null) {
                rootWord = getWordDefinitions("H${word.strongsHeb}")
                    .maxBy { it.weight }.toDomainModel()
                insertRootWord(rootWord)
            }

            wordPairs.add(WordPair(word, rootWord))
        }
        return wordPairs
    }
}

fun RootWordResult.toDomainModel() : RootWord {
    val rootWord = RootWord(
        strongs = this.strongs
            .replace(Regex("\\D"), "") //Removing letter
            .toInt(),
        longDefinition = this.longDefinition,
        shortDefinition = this.shortDefinition,
        hebrewWord = this.hebrewWord,
        transliteration = this.transliteration,
        pronunciation = this.pronunciation,
        language = "H",
    )
    return rootWord
}