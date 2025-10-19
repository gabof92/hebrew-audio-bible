package com.gabof92.hebrewaudiobible.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "root_words")
data class RootWordEntity(
    @PrimaryKey
    val strongs: Int,
    @ColumnInfo(name = "long_definition")
    val longDefinition: String,
    @ColumnInfo(name = "short_definition")
    val shortDefinition: String,
    @ColumnInfo(name = "lexeme")
    val hebrewWord: String,
    @ColumnInfo(name = "transliteration")
    val transliteration: String,
    @ColumnInfo(name = "pronunciation")
    val pronunciation: String,
    @ColumnInfo(name = "language")
    val language: String = "H",
)