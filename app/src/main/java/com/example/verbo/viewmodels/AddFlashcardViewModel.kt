package com.example.verbo.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verbo.common.dtos.FlashcardDto
import com.example.verbo.models.repositories.flashcardrepository.IFlashcardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddFlashcardViewModel @Inject constructor(
    private val flashcardRepository: IFlashcardRepository
): ViewModel() {

    val question = MutableLiveData<String>()
    val answer = MutableLiveData<String>()

    fun addFlashcard(deckId: Long) {
        val questionValue = question.value ?: ""
        val answerValue = answer.value ?: ""

        if (questionValue.isNotBlank() && answerValue.isNotBlank()) {
            viewModelScope.launch {
                val newFlashcard = FlashcardDto(wordDefinition = questionValue, wordTranslation = answerValue)
                flashcardRepository.insertFlashcard(newFlashcard, deckId)

                question.postValue("")
                answer.postValue("")
            }
        }
    }
}