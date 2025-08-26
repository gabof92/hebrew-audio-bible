package com.gabof92.hebrewaudiobible.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @ColumnInfo(name = "book_number")
    @PrimaryKey
    val number: Int,

    @ColumnInfo(name = "name")
    val name :String,
    @ColumnInfo(name = "chapters")
    val chapters :Int,
)
