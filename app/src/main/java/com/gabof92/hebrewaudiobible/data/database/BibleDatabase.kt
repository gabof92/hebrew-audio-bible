package com.gabof92.hebrewaudiobible.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gabof92.hebrewaudiobible.data.database.dao.AudioSourceDao
import com.gabof92.hebrewaudiobible.data.database.dao.BookDao
import com.gabof92.hebrewaudiobible.data.database.dao.OriginalWordDao
import com.gabof92.hebrewaudiobible.data.database.dao.RootWordDao
import com.gabof92.hebrewaudiobible.data.database.dao.VerseTimestampDao
import com.gabof92.hebrewaudiobible.data.database.entity.AudioSourceEntity
import com.gabof92.hebrewaudiobible.data.database.entity.BookEntity
import com.gabof92.hebrewaudiobible.data.database.entity.OriginalWordEntity
import com.gabof92.hebrewaudiobible.data.database.entity.RootWordEntity
import com.gabof92.hebrewaudiobible.data.database.entity.VerseTimestampEntity

@Database(
    entities = [OriginalWordEntity::class, VerseTimestampEntity::class, AudioSourceEntity::class, BookEntity::class, RootWordEntity::class],
    version = 1
)
abstract class BibleDatabase : RoomDatabase() {
    abstract fun booksDao(): BookDao
    abstract fun audioSourceDao(): AudioSourceDao
    abstract fun verseTimestampDao(): VerseTimestampDao
    abstract fun originalWordDao(): OriginalWordDao
    abstract fun rootWordDao(): RootWordDao

    /*companion object {
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
    }*/
}