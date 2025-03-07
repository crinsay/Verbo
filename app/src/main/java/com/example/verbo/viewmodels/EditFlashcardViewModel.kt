package com.example.verbo.viewmodels

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verbo.common.dtos.FlashcardDto
import com.example.verbo.models.repositories.flashcardrepository.IFlashcardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditFlashcardViewModel @Inject constructor(
    private val flashcardRepository: IFlashcardRepository
): ViewModel() {
    val wordDefinition = MutableLiveData<String>()
    val wordTranslation = MutableLiveData<String>()
    private var originalWordDefinition: String? = null
    private var originalWordTranslation: String? = null

    val isSaveFlashcardButtonEnabled = MediatorLiveData<Boolean>().apply {
        addSource(wordDefinition) { checkFields() }
        addSource(wordTranslation) { checkFields() }
    }

    fun getFlashcardById(flashcardId: Long) {
        viewModelScope.launch {
            val flashcard = flashcardRepository.getFlashcardById(flashcardId)

            wordDefinition.postValue(flashcard.wordDefinition)
            wordTranslation.postValue(flashcard.wordTranslation)
            originalWordDefinition = flashcard.wordDefinition
            originalWordTranslation = flashcard.wordTranslation
        }
    }

    suspend fun updateFlashcard(flashcardId: Long, deckId: Long) {
        val flashcardDto = FlashcardDto(
            flashcardId = flashcardId,
            wordDefinition = wordDefinition.value!!.trim(),
            wordTranslation = wordTranslation.value!!.trim()
        )

        flashcardRepository.updateFlashcard(flashcardDto, deckId)
    }

    //Validation:
    private fun checkFields() {
        isSaveFlashcardButtonEnabled.value = canSaveFlashcard()
    }

    private fun canSaveFlashcard(): Boolean {
        return wordDefinition.value?.isNotBlank() == true
                && wordTranslation.value?.isNotBlank() == true
                && (wordDefinition.value!!.trim() != originalWordDefinition
                || wordTranslation.value!!.trim() != originalWordTranslation)
    }
}