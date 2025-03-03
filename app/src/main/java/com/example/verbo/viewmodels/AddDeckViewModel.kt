package com.example.verbo.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verbo.common.dtos.DeckDto
import com.example.verbo.models.repositories.deckrepository.IDeckRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddDeckViewModel @Inject constructor(
    private val deckRepository: IDeckRepository
): ViewModel() {
    val deckName = MutableLiveData<String>()
    private var currentLanguageId: Long = -1L
    var deckId = 0L

    fun setLanguageId(languageId: Long) {
        Log.d("AddWordFragment", "Otrzymano languageId: ${languageId}")
        currentLanguageId = languageId
    }

    fun getDeck(deckId: Long) {
        viewModelScope.launch {
            val deck = deckRepository.getDeckById(deckId)
            deckName.postValue(deck.name)
        }
    }


    suspend fun saveDeck() {

            val deckNameValue = deckName.value!!.trim()

            val deckDto = DeckDto(
                deckId = 0L,
                name = deckNameValue,
            )

            val newDeckId = deckRepository.insertDeck(deckDto, currentLanguageId)
            deckId = newDeckId


    }
}