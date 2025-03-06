package com.example.verbo.viewmodels

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verbo.common.dtos.DeckDto
import com.example.verbo.models.repositories.deckrepository.IDeckRepository
import com.example.verbo.models.repositories.flashcardrepository.IFlashcardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import com.example.verbo.common.dtos.FlashcardDto
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditDeckViewModel @Inject constructor(
    private val flashcardRepository: IFlashcardRepository,
    private val deckRepository: IDeckRepository
) : ViewModel() {
    val deckName = MutableLiveData<String>()
    private var originalDeckName: String? = null

    private val _flashcards = MutableStateFlow<MutableList<FlashcardDto>>(mutableListOf())
    val flashcards: StateFlow<MutableList<FlashcardDto>> = _flashcards

    enum class DeckNameMode(val relatedText: String) {
        IDLE("Edit"),
        EDIT("Save")
    }
    var deckNameMode = DeckNameMode.IDLE

    val isSaveDeckButtonEnabled = MediatorLiveData<Boolean>().apply {
        addSource(deckName) { checkFields() }
    }


    fun getDeckById(deckId: Long) {
        viewModelScope.launch {
            val deck = deckRepository.getDeckById(deckId)

            deckName.postValue(deck.name)
            originalDeckName = deck.name
        }
    }

    suspend fun saveDeck(deckId: Long, languageId: Long) {
        val deckDto = DeckDto(
            deckId = deckId,
            name = deckName.value!!.trim()
        )
        deckName.value = deckDto.name //To remove whitespaces in EditText as well because we are staying in current fragment after saveDeck.
        originalDeckName = deckDto.name

        deckRepository.updateDeck(deckDto, languageId)
    }

    fun getFlashcardsByDeckId(deckId: Long) {
        viewModelScope.launch {
            val flashcards = flashcardRepository.getFlashcardsByDeckId(deckId)

            _flashcards.value = flashcards.toMutableList()
        }
    }

    suspend fun deleteFlashcard(flashcardToDeleteDto: FlashcardDto){
        flashcardRepository.deleteFlashcard(flashcardToDeleteDto)

        _flashcards.value.remove(flashcardToDeleteDto)
    }

    //Validation:
    private fun checkFields() {
        isSaveDeckButtonEnabled.value = canSaveDeck()
    }

    private fun canSaveDeck(): Boolean {
        return deckName.value?.isNotBlank() == true
                && deckName.value!!.trim() != originalDeckName
    }
}