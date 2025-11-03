package com.gabof92.hebrewaudiobible.data.database

import com.gabof92.hebrewaudiobible.data.LocalDataSource
import com.gabof92.hebrewaudiobible.data.database.dao.AudioSourceDao
import com.gabof92.hebrewaudiobible.data.database.dao.BookDao
import com.gabof92.hebrewaudiobible.data.database.dao.OriginalWordDao
import com.gabof92.hebrewaudiobible.data.database.dao.RootWordDao
import com.gabof92.hebrewaudiobible.data.database.dao.VerseTimestampDao
import com.gabof92.hebrewaudiobible.data.database.entity.BookEntity
import com.gabof92.hebrewaudiobible.data.database.entity.OriginalWordEntity
import com.gabof92.hebrewaudiobible.data.database.entity.RootWordEntity
import com.gabof92.hebrewaudiobible.data.database.entity.VerseTimestampEntity
import com.gabof92.hebrewaudiobible.domain.Book
import com.gabof92.hebrewaudiobible.domain.OriginalWord
import com.gabof92.hebrewaudiobible.domain.RootWord
import com.gabof92.hebrewaudiobible.domain.VerseText
import com.gabof92.hebrewaudiobible.domain.VerseTimeStamp
import javax.inject.Inject

class BibleRoomDataSource @Inject constructor(
    val originalWordDao: OriginalWordDao,
    val audioSourceDao: AudioSourceDao,
    val verseTimestampDao: VerseTimestampDao,
    val booksDao: BookDao,
    val rootWordDao: RootWordDao,
) : LocalDataSource {
    override suspend fun getBook(bookNumber: Int) =
        booksDao.getBookByNumber(bookNumber).toDomainModel()

    override suspend fun getChapterAudioUrl(book: Int, chapter: Int) =
        audioSourceDao.getChapterAudioUrl(book, chapter)

    override suspend fun getVerseWords(book: Int, chapter: Int, verse: Int) =
        originalWordDao.getOriginalVerse(book, chapter, verse).toDomainModel()

    override suspend fun getVerses(book: Int, chapter: Int): List<VerseText> =
        originalWordDao.getVerseTexts(book, chapter)

    override suspend fun getChapterTimestamps(book: Int, chapter: Int) =
        verseTimestampDao.getChapterTimestamps(book, chapter).toDomainModel()

    override suspend fun getRootWord(strongs: Int) =
        rootWordDao.getRootWord(strongs)?.toDomainModel()

    override suspend fun insertRootWord(rootWord: RootWord) =
        rootWordDao.insert(rootWord.fromDomainModel())
}

/* Mappers */
private fun BookEntity.toDomainModel(): Book = Book(
    number = number,
    name = name,
    chapters = chapters,
)

private fun OriginalWordEntity.toDomainModel(): OriginalWord = OriginalWord(
    id = id,
    book = book,
    chapter = chapter,
    verse = verse,
    strongsHeb = strongsHeb,
    original = original,
    transliteration = transliteration,
    translation = translation,
    parsingShort = parsingShort,
    parsingLong = parsingLong,
    hebrewSort = hebrewSort,
    englishSort = englishSort,
)

@JvmName("originalWordEntityListToDomainModel")
private fun List<OriginalWordEntity>.toDomainModel(): List<OriginalWord> =
    map { it.toDomainModel() }

private fun VerseTimestampEntity.toDomainModel(): VerseTimeStamp = VerseTimeStamp(
    book = book,
    chapter = chapter,
    verse = verse,
    millis = millis,
)

@JvmName("verseTimestampEntityListToDomainModel")
private fun List<VerseTimestampEntity>.toDomainModel(): List<VerseTimeStamp> =
    map { it.toDomainModel() }

private fun RootWordEntity.toDomainModel(): RootWord = RootWord(
    strongs = strongs,
    longDefinition = longDefinition,
    shortDefinition = shortDefinition,
    hebrewWord = hebrewWord,
    transliteration = transliteration,
    pronunciation = pronunciation,
)

private fun RootWord.fromDomainModel(): RootWordEntity = RootWordEntity(
    strongs = strongs,
    longDefinition = longDefinition,
    shortDefinition = shortDefinition,
    hebrewWord = hebrewWord,
    transliteration = transliteration,
    pronunciation = pronunciation,
)