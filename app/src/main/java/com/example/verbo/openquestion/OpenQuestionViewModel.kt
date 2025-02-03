package com.example.verbo.openquestion

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
class OpenQuestionViewModel @Inject constructor(
    private val flashCardRepository: IFlashcardRepository
) : ViewModel() {
    private var currentDeckId: Long = -1L
    private var currentQuestionIndex = 0
    private var flashcards: List<FlashcardDto> = listOf()

    val question = MutableLiveData<String>()
    val correctanswer = MutableLiveData<String>()
    val answerfromuser = MutableLiveData<String>()
    val score = MutableLiveData(0)
    val isTestFinished = MutableLiveData(false)

    fun setDeckId(deckId: Long) {
        Log.d("OpenQuestion", "Setting deckId: $deckId")
        currentDeckId = deckId
        loadFlashcards()
    }

    private fun loadFlashcards() {
        viewModelScope.launch {
            flashcards = flashCardRepository.getFlashcardsByDeckId(currentDeckId)
            if (flashcards.isNotEmpty()) {
                showNextQuestion()
            }
        }
    }

    private fun showNextQuestion() {
        if (currentQuestionIndex >= flashcards.size) {
            isTestFinished.value = true
            return
        }

        val currentFlashcard = flashcards[currentQuestionIndex]
        question.value = currentFlashcard.wordDefinition
        correctanswer.value = ""
    }

    fun checkAnswer() {
        val userAnswer = answerfromuser.value?.trim()?.lowercase() ?: ""
        val correctAnswer = flashcards[currentQuestionIndex].wordTranslation.lowercase()

        if (userAnswer == correctAnswer) {
            score.value = (score.value ?: 0) + 1
        }


        correctanswer.value = flashcards[currentQuestionIndex].wordTranslation

        viewModelScope.launch {
            kotlinx.coroutines.delay(1000)
            currentQuestionIndex++
            answerfromuser.value = ""
            showNextQuestion()
        }
    }
}