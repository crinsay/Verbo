package com.example.verbo.editset

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
class EditSetViewModel@Inject constructor(
    private val deckRepository: IDeckRepository
) : ViewModel() {
    val deckName = MutableLiveData<String>()
    private var currentDeckId: Long = -1L
    private var currentLanguageId: Long = -1L
    var deckId = 0L

    fun setDeckId(deckId: Long) {
        Log.d("EditSetViewModel", "Otrzymano deckId: $deckId")
        currentDeckId = deckId
    }
    fun setLanguageId(languageId: Long) {
        Log.d("AddWordFragment", "Otrzymano languageId: ${languageId}")
        currentLanguageId = languageId
    }

    fun loadDeckData() {
        viewModelScope.launch {
            val deck = deckRepository.getDeckById(currentDeckId)
            deckName.postValue(deck.name)
        }
    }

    fun updateDeckName(newName: String) {
        deckName.value = newName
    }
    suspend fun updateDeck() {
        val deckNameValue = deckName.value!!.trim()

        val deckDto = DeckDto(
            deckId = currentDeckId,
            name = deckNameValue
        )
        val oldDeckId = deckRepository.updateDeck(deckDto, currentLanguageId)
        deckId = oldDeckId
    }
}