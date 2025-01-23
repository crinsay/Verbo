package com.example.verbo.models.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.verbo.models.DatabaseConstants
import com.example.verbo.models.entities.Flashcard

@Dao
interface FlashcardDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFlashcard(newFlashcard: Flashcard)

    @Update
    suspend fun updateFlashcard(updatedFlashcard: Flashcard)

    @Delete
    suspend fun deleteFlashcard(flashcardToDelete: Flashcard)

    @Query("""
        SELECT flashcardId, wordDefinition, wordTranslation, deckId
        FROM ${DatabaseConstants.TableNames.FLASHCARD}
        WHERE flashcardId = :flashcardId
        LIMIT 1
    """)
    suspend fun getFlashcardById(flashcardId: Long): Flashcard

    @Query("""
        SELECT flashcardId, wordDefinition, wordTranslation, deckId
        FROM ${DatabaseConstants.TableNames.FLASHCARD}
        WHERE deckId = :deckId
        ORDER BY wordDefinition COLLATE NOCASE, wordTranslation COLLATE NOCASE
    """)
    suspend fun getFlashcardsByDeckId(deckId: Long): List<Flashcard>
}