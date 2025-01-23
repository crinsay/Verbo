package com.example.verbo.common.mappers

import com.example.verbo.common.dtos.DeckDto
import com.example.verbo.models.entities.Deck

fun Deck.mapToDeckDto(): DeckDto {
    return DeckDto(
        deckId = this.deckId,
        name = this.name
    )
}

fun List<Deck>.mapToDeckDtos(): List<DeckDto> {
    return this.map {
        it.mapToDeckDto()
    }
}

fun DeckDto.mapToDeck(): Deck {
    return Deck(
        deckId = this.deckId,
        name = this.name,
        languageId = 0L
    )
}




