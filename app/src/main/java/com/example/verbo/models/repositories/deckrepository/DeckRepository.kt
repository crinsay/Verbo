package com.example.verbo.models.repositories.deckrepository

import com.example.verbo.common.dtos.DeckDto
import com.example.verbo.common.mappers.mapToDeck
import com.example.verbo.common.mappers.mapToDeckDto
import com.example.verbo.common.mappers.mapToDeckDtos
import com.example.verbo.models.daos.DeckDao
import javax.inject.Inject

class DeckRepository @Inject constructor(
    private val deckDao: DeckDao
): IDeckRepository {
    override suspend fun insertDeck(newDeckDto: DeckDto, languageId: Long) {
        val newDeck = newDeckDto.mapToDeck()
        newDeck.languageId = languageId

        deckDao.insertDeck(newDeck)
    }

    override suspend fun updateDeck(updatedDeckDto: DeckDto, languageId: Long) {
        val updatedDeck = updatedDeckDto.mapToDeck()
        updatedDeck.languageId = languageId

        deckDao.updateDeck(updatedDeck)
    }

    override suspend fun deleteDeck(deckToDeleteDto: DeckDto) {
        val deckToDelete = deckToDeleteDto.mapToDeck()

        deckDao.deleteDeck(deckToDelete)
    }

    override suspend fun getDecksByLanguageId(languageId: Long): List<DeckDto> {
        val decks = deckDao.getDecksByLanguageId(languageId)
        val deckDtos = decks.mapToDeckDtos()

        return deckDtos
    }

    override suspend fun getDeckById(deckId: Long): DeckDto {
        val deck = deckDao.getDeckById(deckId)
        val deckDto = deck.mapToDeckDto()

        return deckDto
    }
}

