package com.gabof92.hebrewaudiobible.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [OriginalWord::class, VerseTimestamp::class, AudioSource::class], version = 1)
abstract class BibleDatabase: RoomDatabase() {
    abstract fun originalWordDao(): OriginalWordDao
    abstract fun verseTimestampDao(): VerseTimestampDao

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
                    .createFromAsset("psalms_db_asset.db") // This loads the existing database
                    .build()
                INSTANCE = instance

                instance
            }
        }
    }
}