package com.gabof92.hebrewaudiobible.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RootWordDao {

    @Query("SELECT * FROM root_words WHERE strongs = :strongs AND language = :language")
    suspend fun getRootWord(language: String, strongs: Int): RootWord?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(rootWord: RootWord)

}