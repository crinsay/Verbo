package com.example.verbo.models.repositories.flashcardrepository

import com.example.verbo.common.dtos.FlashcardDto

interface IFlashcardRepository {
    suspend fun insertFlashcard(newFlashcardDto: FlashcardDto, deckId: Long)
    suspend fun updateFlashcard(updatedFlashcardDto: FlashcardDto, deckId: Long)
    suspend fun deleteFlashcard(flashcardDtoToDelete: FlashcardDto)
    suspend fun getFlashcardById(flashcardDtoId: Long): FlashcardDto
    suspend fun getFlashcardsByDeckId(deckId: Long): List<FlashcardDto>
}