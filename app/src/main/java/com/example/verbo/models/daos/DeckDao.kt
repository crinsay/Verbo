package com.example.verbo.models.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.verbo.models.DatabaseConstants
import com.example.verbo.models.entities.Deck

@Dao
interface DeckDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDeck(newDeck: Deck)

    @Update
    suspend fun updateDeck(updatedDeck: Deck)

    @Delete
    suspend fun deleteDeck(deckToDelete: Deck)

    @Query("""
        SELECT deckId, name, languageId
        FROM ${DatabaseConstants.TableNames.DECK}
        WHERE languageId = :languageId
        ORDER BY name COLLATE NOCASE
    """)
    suspend fun getDecksByLanguageId(languageId: Long): List<Deck>

    @Query("""
        SELECT deckId, name, languageId
        FROM ${DatabaseConstants.TableNames.DECK}
        WHERE deckId = :deckId
        LIMIT 1
    """)
    suspend fun getDeckById(deckId: Long): Deck
}