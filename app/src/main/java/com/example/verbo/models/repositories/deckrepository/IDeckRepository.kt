package com.example.verbo.models.repositories.deckrepository

import com.example.verbo.common.dtos.DeckDto
import com.example.verbo.models.entities.Deck

interface IDeckRepository {
    suspend fun insertDeck(newDeckDto: DeckDto, languageId: Long) : Long
    suspend fun updateDeck(updatedDeckDto: DeckDto, languageId: Long) : Long
    suspend fun deleteDeck(deckToDeleteDto: DeckDto)
    suspend fun getDecksByLanguageId(languageId: Long): List<DeckDto>
    suspend fun getDeckById(deckId: Long): DeckDto
}