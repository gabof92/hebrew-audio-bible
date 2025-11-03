package com.gabof92.hebrewaudiobible.di

import android.app.Application
import androidx.room.Room
import com.gabof92.hebrewaudiobible.data.LocalDataSource
import com.gabof92.hebrewaudiobible.data.RemoteDataSource
import com.gabof92.hebrewaudiobible.data.database.BibleDatabase
import com.gabof92.hebrewaudiobible.data.database.BibleRoomDataSource
import com.gabof92.hebrewaudiobible.data.network.BollsBibleDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDatabase(app: Application) = Room.databaseBuilder(
        app, BibleDatabase::class.java, "bible_app"
    ).createFromAsset("hebrew_bible_asset.db") // This loads the existing database
        .build()

    @Singleton
    @Provides
    fun provideBookDao(db: BibleDatabase) = db.booksDao()

    @Singleton
    @Provides
    fun provideAudioSourceDao(db: BibleDatabase) = db.audioSourceDao()

    @Singleton
    @Provides
    fun provideVerseTimestampDao(db: BibleDatabase) = db.verseTimestampDao()

    @Singleton
    @Provides
    fun provideOriginalWordDao(db: BibleDatabase) = db.originalWordDao()

    @Singleton
    @Provides
    fun provideRootWordDao(db: BibleDatabase) = db.rootWordDao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class AppDataModule{
    @Binds
    abstract fun bindLocalDataSource(localDataSource: BibleRoomDataSource): LocalDataSource

    @Binds
    abstract fun bindRemoteDatasource(remoteDatasource: BollsBibleDataSource): RemoteDataSource
}
