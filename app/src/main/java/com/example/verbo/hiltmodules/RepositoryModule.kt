package com.example.verbo.hiltmodules

import com.example.verbo.models.daos.LanguageDao
import com.example.verbo.models.repositories.deckrepository.DeckRepository
import com.example.verbo.models.repositories.deckrepository.IDeckRepository
import com.example.verbo.models.repositories.flashcardrepository.FlashcardRepository
import com.example.verbo.models.repositories.flashcardrepository.IFlashcardRepository
import com.example.verbo.models.repositories.languagerepository.ILanguageRepository
import com.example.verbo.models.repositories.languagerepository.LanguageRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindLanguageRepository(languageRepository: LanguageRepository): ILanguageRepository

    @Binds
    @Singleton
    abstract fun bindDeckRepository(deckRepository: DeckRepository): IDeckRepository

    @Binds
    @Singleton
    abstract fun bindFlashcardRepository(flashcardRepository: FlashcardRepository): IFlashcardRepository
}