package com.example.verbo.addset

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verbo.common.dtos.DeckDto
import com.example.verbo.models.repositories.deckrepository.DeckRepository
import com.example.verbo.models.repositories.deckrepository.IDeckRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddSetViewModel @Inject constructor(
    private val deckRepository: IDeckRepository
): ViewModel() {
    val deckName = MutableLiveData<String>()
    private var currentLanguageId: Long = -1L

    fun setLanguageId(languageId: Long) {
        currentLanguageId = languageId
    }

    fun getDeck(deckId: Long) {
        viewModelScope.launch {
            val deck = deckRepository.getDeckById(deckId)
            deckName.postValue(deck.name)
        }
    }


    fun saveDeck(deckId: Long) {
        viewModelScope.launch {
            val deckNameValue = deckName.value?.trim() ?: return@launch
            if (currentLanguageId == -1L) {

                return@launch
            }

            val deckDto = DeckDto(
                deckId = deckId,
                name = deckNameValue,
            )

            if (deckId != 0L) {
                deckRepository.updateDeck(deckDto, currentLanguageId)
            } else {
                deckRepository.insertDeck(deckDto, currentLanguageId)
            }
        }
    }
}