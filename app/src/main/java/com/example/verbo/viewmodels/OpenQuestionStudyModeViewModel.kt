package com.example.verbo.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verbo.common.dtos.FlashcardDto
import com.example.verbo.models.repositories.flashcardrepository.IFlashcardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OpenQuestionStudyModeViewModel @Inject constructor(
    private val flashcardRepository: IFlashcardRepository
) : ViewModel() {
    private var currentFlashcardNumber = 0
    private val flashcards: MutableList<FlashcardDto> = mutableListOf()

    private val _question = MutableLiveData<String>()
    val question: LiveData<String> = _question

    private val _correctAnswer = MutableLiveData<String>()
    val correctAnswer: LiveData<String> = _correctAnswer

    val userAnswer = MutableLiveData<String>()

    private val _isStudyFinished = MutableLiveData(false)
    val isStudyFinished: LiveData<Boolean> = _isStudyFinished

    val isNextFlashcardButtonEnabled = MediatorLiveData<Boolean>().apply {
        addSource(userAnswer) { checkFields() }
    }

    fun prepareFlashcards(deckId: Long) {
        viewModelScope.launch {
            val resultFlashcards = flashcardRepository.getFlashcardsByDeckId(deckId)
            flashcards.addAll(resultFlashcards.shuffled())

            showNextFlashcard()
        }
    }

    private fun showNextFlashcard() {
        if (isStudyFinished()) {
            _isStudyFinished.value = true
            return
        }

        val currentFlashcard = flashcards[currentFlashcardNumber - 1]

        _question.value = currentFlashcard.wordDefinition
        _correctAnswer.value = "???"
        userAnswer.value = ""
    }

    fun checkAnswerAndShowNextFlashcard() {
        viewModelScope.launch {
            _correctAnswer.value = flashcards[currentFlashcardNumber - 1].wordTranslation

            //Wait a little bit:
            delay(1500)

            showNextFlashcard()
        }
    }

    private fun isStudyFinished(): Boolean {
        return ++currentFlashcardNumber > flashcards.size
    }

    //Validation:
    private fun checkFields() {
        isNextFlashcardButtonEnabled.value = canGoToNextFlashcard()
    }

    private fun canGoToNextFlashcard(): Boolean {
        return userAnswer.value?.isNotBlank() == true
    }
}