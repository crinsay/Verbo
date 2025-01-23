package com.example.verbo.common.mappers

import com.example.verbo.common.dtos.FlashcardDto
import com.example.verbo.models.entities.Flashcard


fun Flashcard.mapToFlashcardDto(): FlashcardDto
{
    return FlashcardDto(
        flashcardId = this.flashcardId,
        wordDefinition = this.wordDefinition,
        wordTranslation = this.wordTranslation
    )
}

fun FlashcardDto.mapToFlashcard(): Flashcard
{
    return Flashcard(
        flashcardId = this.flashcardId,
        wordDefinition = this.wordDefinition,
        wordTranslation = this.wordTranslation,
        deckId = 0L
    )
}

fun List<Flashcard>.mapToFlashcardDtos(): List<FlashcardDto>
{
    return this.map {
        it.mapToFlashcardDto()
    }
}