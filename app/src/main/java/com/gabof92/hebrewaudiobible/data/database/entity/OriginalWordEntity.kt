package com.gabof92.hebrewaudiobible.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "original_words")
data class OriginalWordEntity(
    @PrimaryKey
    val id: Int,

    @ColumnInfo(name = "heb_sort")
    val hebrewSort :Int,
    @ColumnInfo(name = "greek_sort")
    val greekSort :Int,
    @ColumnInfo(name = "english_sort")
    val englishSort :Int,
    @ColumnInfo(name = "book")
    val book :Int,
    @ColumnInfo(name = "chapter")
    val chapter :Int,
    @ColumnInfo(name = "verse")
    val verse :Int,


    val language :String,
    val original :String,
    @ColumnInfo(name = "original_alt")
    val altOrig :String,
    val transliteration :String,
    @ColumnInfo(name = "parsing_short")
    val parsingShort :String,
    @ColumnInfo(name = "parsing_long")
    val parsingLong :String,

    @ColumnInfo(name = "strongs_heb")
    val strongsHeb :Int,
    @ColumnInfo(name = "strongs_greek")
    val strongsGreek :Int,

    @ColumnInfo(name = "verse_text")
    val verseText :String,
    @ColumnInfo(name = "title_html")
    val title :String,
    @ColumnInfo(name = "crossref_html")
    val crossRef :String,
    @ColumnInfo(name = "paragraph_html")
    val paragraph :String,
    @ColumnInfo(name = "quote_open")
    val quoteOpen :String,
    @ColumnInfo(name = "translation_bsb")
    val translation :String,
    val punctuation :String,
    @ColumnInfo(name = "quote_close")
    val quoteClose :String,
    val footnotes :String,
    @ColumnInfo(name = "end_punctuation")
    val end :String,
)