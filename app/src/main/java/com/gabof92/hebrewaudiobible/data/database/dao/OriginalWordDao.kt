package com.gabof92.hebrewaudiobible.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.gabof92.hebrewaudiobible.data.database.entity.OriginalWordEntity
import com.gabof92.hebrewaudiobible.domain.VerseText

@Dao
interface OriginalWordDao {
    @Query("SELECT * FROM original_words WHERE id = :id")
    suspend fun getOriginalWord(id: Int): OriginalWordEntity

    @Query("SELECT * FROM original_words " +
            "WHERE BOOK=:book AND CHAPTER=:chapter AND VERSE=:verse " +
            "ORDER BY heb_sort ASC")
    suspend fun getOriginalVerse(book: Int, chapter: Int, verse: Int): List<OriginalWordEntity>

    @Query("""
        WITH hebrew AS (
            SELECT
                verse,
                GROUP_CONCAT(original, ' ') AS hebrew,
                GROUP_CONCAT(transliteration, ' ') AS transliteration
            FROM (
                SELECT *
                FROM original_words
                WHERE book = :book AND chapter = :chapter
                ORDER BY verse, heb_sort
            )
            GROUP BY verse
        ),
        english AS (
            SELECT
                verse,
                GROUP_CONCAT(translation_bsb, ' ') AS translation
            FROM (
                SELECT *
                FROM original_words
                WHERE book = :book AND chapter = :chapter
                ORDER BY verse, english_sort
            )
            GROUP BY verse
        )
        SELECT
            h.verse,
            h.hebrew,
            h.transliteration,
            e.translation
        FROM hebrew h
        JOIN english e
            ON h.verse = e.verse
        ORDER BY h.verse
    """)
    suspend fun getVerseTexts( book: Int, chapter: Int): List<VerseText>
}
