package com.example.verbo.addword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verbo.common.dtos.FlashcardDto
import com.example.verbo.models.repositories.flashcardrepository.IFlashcardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddWordViewModel @Inject constructor(
    private val flashcardRepository: IFlashcardRepository
): ViewModel() {

    fun addFlashcard(deckId: Long, question: String, answer: String) {
        viewModelScope.launch {
            val newFlashcard = FlashcardDto(
                wordDefinition = question,
                wordTranslation = answer
            )
            flashcardRepository.insertFlashcard(newFlashcard, deckId)
        }
    }
}