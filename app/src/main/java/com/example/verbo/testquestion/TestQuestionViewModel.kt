package com.example.verbo.testquestion

import android.graphics.Color
import android.util.Log
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
class TestQuestionViewModel @Inject constructor(
    private val flashCardRepository: IFlashcardRepository
) : ViewModel() {
    private var currentDeckId: Long = -1L
    var currentQuestionIndex = 0
    private var correctAnswerIndex = 0
    private var flashcards: List<FlashcardDto> = listOf()

    val answer1 = MutableLiveData<String>()
    val answer2 = MutableLiveData<String>()
    val answer3 = MutableLiveData<String>()
    val answer4 = MutableLiveData<String>()
    val question = MutableLiveData<String>()
    val score = MutableLiveData(0)
    val isTestFinished = MutableLiveData(false)

    val buttonColors = MutableLiveData<List<Int>>(List(4) { Color.parseColor("#e0da2f") })
    private val defaultColor = Color.parseColor("#e0da2f")
    private val correctColor = Color.parseColor("#4CAF50")
    private val wrongColor = Color.parseColor("#F44336")

    private val questionDelay = 1000L

    private val totalQuestions = 5
    private lateinit var selectedFlashcards: List<FlashcardDto>
    val whichQuestion = MutableLiveData<String>()

    fun setDeckId(deckId: Long) {
        Log.d("CloseQuestion", "Setting deckId: $deckId")
        currentDeckId = deckId
        loadFlashcards()
    }

    private fun loadFlashcards() {
        viewModelScope.launch {
            flashcards = flashCardRepository.getFlashcardsByDeckId(currentDeckId)
            Log.d("CloseQuestion", "Loaded flashcards: ${flashcards.size}")
            if (flashcards.size >= 4) {
                selectedFlashcards = flashcards.shuffled().take(totalQuestions)
                showNextQuestion()
            }
        }
    }

    private fun showNextQuestion() {
        if (currentQuestionIndex >= totalQuestions) {
            isTestFinished.value = true
            return
        }

        whichQuestion.value = "Pytanie ${currentQuestionIndex + 1} z $totalQuestions"

        val questionFlashcard = selectedFlashcards[currentQuestionIndex]
        val incorrectAnswers = flashcards.filter { it.flashcardId != questionFlashcard.flashcardId }
            .shuffled()
            .take(3)
            .map { it.wordTranslation }

        val allAnswers = (incorrectAnswers + questionFlashcard.wordTranslation).shuffled()
        correctAnswerIndex = allAnswers.indexOf(questionFlashcard.wordTranslation)

        question.value = questionFlashcard.wordDefinition
        answer1.value = allAnswers[0]
        answer2.value = allAnswers[1]
        answer3.value = allAnswers[2]
        answer4.value = allAnswers[3]
    }

    fun checkAnswer(selectedAnswerIndex: Int) {
        viewModelScope.launch {
            val newColors = MutableList(4) { defaultColor }
            newColors[correctAnswerIndex] = correctColor

            if (selectedAnswerIndex == correctAnswerIndex) {
                score.value = (score.value ?: 0) + 1
            } else {
                newColors[selectedAnswerIndex] = wrongColor
            }

            buttonColors.value = newColors

            delay(questionDelay)

            currentQuestionIndex++
            buttonColors.value = List(4) { defaultColor }
            showNextQuestion()
        }
    }
}