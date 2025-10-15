package com.gabof92.hebrewaudiobible.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [OriginalWord::class, VerseTimestamp::class, AudioSource::class, Book::class, RootWord::class], version = 1)
abstract class BibleDatabase: RoomDatabase() {
    abstract fun originalWordDao(): OriginalWordDao
    abstract fun verseTimestampDao(): VerseTimestampDao
    abstract fun audioSourceDao(): AudioSourceDao
    abstract fun booksDao(): BooksDao
    abstract fun rootWordDao(): RootWordDao

    companion object{
        @Volatile
        private var INSTANCE: BibleDatabase? = null
        fun getDatabase(context: Context): BibleDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    BibleDatabase::class.java,
                    "bible_app"
                )
                    .createFromAsset("hebrew_bible_asset.db") // This loads the existing database
                    .build()
                INSTANCE = instance

                instance
            }
        }
    }
}