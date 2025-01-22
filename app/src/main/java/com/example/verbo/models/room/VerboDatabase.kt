package com.example.verbo.models.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.verbo.models.daos.DeckDao
import com.example.verbo.models.daos.FlashcardDao
import com.example.verbo.models.daos.LanguageDao
import com.example.verbo.models.entities.Deck
import com.example.verbo.models.entities.Flashcard
import com.example.verbo.models.entities.Language

@Database(entities = [
    Language::class,
    Deck::class,
    Flashcard::class
],
    version = 1,
    exportSchema = false
)
abstract class VerboDatabase: RoomDatabase() {
    abstract fun LanguageDao(): LanguageDao
    abstract fun DeckDao(): DeckDao
    abstract fun FlashcardDao(): FlashcardDao
}