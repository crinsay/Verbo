package com.example.verbo.viewmodels

import android.util.Log
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
    private var currentDeckId: Long = -1L
    val question = MutableLiveData<String>()
    val answer = MutableLiveData<String>()

    fun setDeckId(deckId: Long) {
        Log.d("EditWordViewModel", "Otrzymano deckId: $deckId")
        currentDeckId = deckId
    }

    fun loadFlashcard(flashcardId: Long) {
        viewModelScope.launch {
            val flashcard = flashcardRepository.getFlashcardById(flashcardId)

            question.postValue(flashcard.wordDefinition)
            answer.postValue(flashcard.wordTranslation)
        }
    }

    fun updateFlashcard(flashcardId: Long) {
        val questionValue = question.value ?: ""
        val answerValue = answer.value ?: ""

        if (questionValue.isNotBlank() && answerValue.isNotBlank()) {
            viewModelScope.launch {
                val updatedFlashcard = FlashcardDto(
                    flashcardId = flashcardId,
                    wordDefinition = questionValue,
                    wordTranslation = answerValue
                )
                flashcardRepository.updateFlashcard(updatedFlashcard, currentDeckId)
            }
        }
    }
}