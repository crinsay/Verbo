package com.example.verbo.models.repositories.flashcardrepository

import com.example.verbo.common.dtos.FlashcardDto
import com.example.verbo.models.daos.FlashcardDao
import com.example.verbo.common.mappers.mapToFlashcard
import com.example.verbo.common.mappers.mapToFlashcardDto
import com.example.verbo.common.mappers.mapToFlashcardDtos
import javax.inject.Inject

class FlashcardRepository@Inject constructor(
    private val flashcardDao: FlashcardDao
): IFlashcardRepository {
    override suspend fun insertFlashcard(newFlashcardDto: FlashcardDto, deckId: Long) {
        val newFlashcard = newFlashcardDto.mapToFlashcard()
        newFlashcard.deckId = deckId
        flashcardDao.insertFlashcard(newFlashcard)
    }

    override suspend fun updateFlashcard(updatedFlashcardDto: FlashcardDto, deckId: Long) {
        val updatedFlashcard = updatedFlashcardDto.mapToFlashcard()
        updatedFlashcard.deckId = deckId
        flashcardDao.updateFlashcard(updatedFlashcard)
    }

    override suspend fun deleteFlashcard(flashcardDtoToDelete: FlashcardDto) {
        val flashcardToDelete = flashcardDtoToDelete.mapToFlashcard()
        flashcardDao.deleteFlashcard(flashcardToDelete)
    }

    override suspend fun getFlashcardById(flashcardDtoId: Long): FlashcardDto {
        val flashcard = flashcardDao.getFlashcardById(flashcardDtoId)
        val flashcardDto = flashcard.mapToFlashcardDto()
        return flashcardDto
    }

    override suspend fun getFlashcardsByDeckId(deckId: Long): List<FlashcardDto> {
        val flashcards = flashcardDao.getFlashcardsByDeckId(deckId)
        val flashcardDtos = flashcards.mapToFlashcardDtos()
        return flashcardDtos
    }
}