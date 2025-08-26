package com.gabof92.hebrewaudiobible.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "audio_sources")
data class AudioSource(
    @PrimaryKey
    val id: Int,

    @ColumnInfo(name = "book")
    val book :Int,
    @ColumnInfo(name = "chapter")
    val verse :Int,
    @ColumnInfo(name = "collection")
    val collection :String,
    @ColumnInfo(name = "url")
    val url :String,
)