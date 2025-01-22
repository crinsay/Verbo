package com.example.verbo.hiltmodules

import android.content.Context
import androidx.room.Room
import com.example.verbo.models.DatabaseConstants
import com.example.verbo.models.daos.DeckDao
import com.example.verbo.models.daos.FlashcardDao
import com.example.verbo.models.daos.LanguageDao
import com.example.verbo.models.room.VerboDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Provides
    @Singleton
    fun provideVerboDatabase(@ApplicationContext context: Context): VerboDatabase {
        return Room.databaseBuilder(
            context,
            VerboDatabase::class.java,
            DatabaseConstants.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideLangaugeDao(database: VerboDatabase): LanguageDao {
        return database.LanguageDao()
    }

    @Provides
    @Singleton
    fun provideDeckDao(database: VerboDatabase): DeckDao {
        return database.DeckDao()
    }

    @Provides
    @Singleton
    fun provideFlashcardDao(database: VerboDatabase): FlashcardDao {
        return database.FlashcardDao()
    }
}