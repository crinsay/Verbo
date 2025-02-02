package com.example.verbo.editset

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verbo.common.dtos.DeckDto
import com.example.verbo.common.dtos.LanguageDto
import com.example.verbo.models.repositories.deckrepository.IDeckRepository
import com.example.verbo.models.repositories.flashcardrepository.IFlashcardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import com.example.verbo.common.dtos.FlashcardDto
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditSetViewModel@Inject constructor(
    private val flashCardRepository: IFlashcardRepository,
    private val deckRepository: IDeckRepository
) : ViewModel() {
    val deckName = MutableLiveData<String>()
    private var currentDeckId: Long = -1L
    private var currentLanguageId: Long = -1L
    //var deckId = 0L
    //Podobno lepsze emptyList od MutableListof()
    private val _flashes = MutableStateFlow<List<FlashcardDto>>(emptyList())
    val flashes: StateFlow<List<FlashcardDto>> = _flashes

    //Ustawianie Id decku oraz Language pod deck
    fun setDeckId(deckId: Long) {
        Log.d("EditSetViewModel", "Otrzymano deckId: $deckId")
        currentDeckId = deckId
    }
    fun setLanguageId(languageId: Long) {
        Log.d("AddWordFragment", "Otrzymano languageId: ${languageId}")
        currentLanguageId = languageId
    }

    //Ładowanie Decku oraz Ładowanie Nazwy
    fun loadDeckData() {
        viewModelScope.launch {
            try {
                val deck = deckRepository.getDeckById(currentDeckId)
                deckName.postValue(deck.name)
                val flashcards = flashCardRepository.getFlashcardsByDeckId(currentDeckId)
                _flashes.value = flashcards
            } catch (e: Exception) {
                Log.e("EditSetViewModel", "Error loading deck data", e)
            }
        }
    }

    //CRUD
    /*
    suspend fun deleteWord(wordToDelteDto: FlashcardDto){
        flashCardRepository.deleteFlashcard(wordToDelteDto)
        _flashes.value = _flashes.value.filter {
            it.flashcardId != wordToDelteDto.flashcardId
        }
    }

     */
    suspend fun deleteWord(wordToDelteDto: FlashcardDto){
        flashCardRepository.deleteFlashcard(wordToDelteDto)
        //_flashes.value.remove(wordToDelteDto) Nie działa value remove jakos
    }

    fun updateDeckName(newName: String) {
        Log.d("EditSetViewModel", "updateDeckName called with: $newName")
        viewModelScope.launch {
            saveDeckNameToDatabase(newName)
        }
    }
    private suspend fun saveDeckNameToDatabase(newName: String) {
        val deckDto = DeckDto(
            deckId = currentDeckId,
            name = newName
        )
        Log.d("EditSetViewModel", "Saving to DB: ${deckDto.name}")

        deckRepository.updateDeck(deckDto, currentLanguageId)
    }
    /*
    suspend fun updateDeck() {
        val deckNameValue = deckName.value!!.trim()

        val deckDto = DeckDto(
            deckId = currentDeckId,
            name = deckNameValue
        )
        deckRepository.updateDeck(deckDto, currentLanguageId)

    }
    */

}