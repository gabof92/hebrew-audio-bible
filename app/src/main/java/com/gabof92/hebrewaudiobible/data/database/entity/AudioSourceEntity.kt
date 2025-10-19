package com.gabof92.hebrewaudiobible.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "audio_sources")
data class AudioSourceEntity(
    @PrimaryKey
    val id: Int,

    @ColumnInfo(name = "book")
    val book :Int,
    @ColumnInfo(name = "chapter")
    val chapter :Int,
    @ColumnInfo(name = "collection")
    val collection :String,
    @ColumnInfo(name = "url")
    val url :String,
)