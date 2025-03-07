package com.example.verbo.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.verbo.common.dtos.DeckDto
import com.example.verbo.models.repositories.deckrepository.IDeckRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class AddDeckViewModel @Inject constructor(
    private val deckRepository: IDeckRepository
): ViewModel() {
    val deckName = MutableLiveData<String>()
    var deckId = 0L

    val isSaveDeckButtonEnabled = MediatorLiveData<Boolean>().apply {
        addSource(deckName) { checkFields() }
    }

    suspend fun saveDeck(languageId: Long) {
        val deckDto = DeckDto(
            deckId = 0L,
            name = deckName.value!!.trim()
        )

        val newDeckId = deckRepository.insertDeck(deckDto, languageId)
        deckId = newDeckId
    }

    //Validation:
    private fun checkFields() {
        isSaveDeckButtonEnabled.value = canSaveDeck()
    }

    private fun canSaveDeck(): Boolean {
        return deckName.value?.isNotBlank() == true
    }
}