package com.gabof92.hebrewaudiobible.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "verse_timestamps")
data class VerseTimestampEntity(
    @PrimaryKey
    val id: Int,

    @ColumnInfo(name = "book")
    val book :Int,
    @ColumnInfo(name = "chapter")
    val chapter :Int,
    @ColumnInfo(name = "verse")
    val verse :Int,
    @ColumnInfo(name = "audio_collection")
    val collection :String,
    @ColumnInfo(name = "milliseconds")
    val millis :Int,
)