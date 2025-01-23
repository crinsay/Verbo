package com.example.verbo.common.dtos

data class FlashcardDto(
    val flashcardId: Long = 0L,
    val wordDefinition: String,
    val wordTranslation: String
)
