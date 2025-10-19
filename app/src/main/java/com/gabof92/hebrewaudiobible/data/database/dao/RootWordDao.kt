package com.gabof92.hebrewaudiobible.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gabof92.hebrewaudiobible.data.database.entity.RootWordEntity

@Dao
interface RootWordDao {

    @Query("SELECT * FROM root_words WHERE strongs = :strongs AND language = 'H'")
    suspend fun getRootWord(strongs: Int): RootWordEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(rootWord: RootWordEntity)

}