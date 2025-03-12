package com.example.verbo.common.dtos

//For simplicity we will use only one Dto for creating, updating, deleting and displaying decks in UI.
//Important note: if you create the DeckDto for insertDeck, the deckId should be 0!
data class DeckDto(
    val deckId: Long = 0L,
    val name: String
)
