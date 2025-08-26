package com.gabof92.hebrewaudiobible.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "verse_timestamps")
data class VerseTimestamp(
    @PrimaryKey
    val id: Int,

    @ColumnInfo(name = "verse")
    val verse :Int,
    @ColumnInfo(name = "audio_source")
    val source :Int,
    @ColumnInfo(name = "milliseconds")
    val millis :Int,
)