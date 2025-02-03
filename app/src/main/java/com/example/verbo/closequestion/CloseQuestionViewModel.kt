package com.example.verbo.closequestion

import android.graphics.Color
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verbo.common.dtos.FlashcardDto
import com.example.verbo.models.repositories.deckrepository.DeckRepository
import com.example.verbo.models.repositories.deckrepository.IDeckRepository
import com.example.verbo.models.repositories.flashcardrepository.IFlashcardRepository
import com.example.verbo.models.repositories.languagerepository.ILanguageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CloseQuestionViewModel @Inject constructor(
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
                showNextQuestion()
            }
        }
    }

    private fun showNextQuestion() {
        if (currentQuestionIndex >= flashcards.size) {
            isTestFinished.value = true
            return
        }

        val questionFlashcard = flashcards[currentQuestionIndex]
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
            newColors[correctAnswerIndex] = correctColor  // pokazujemy prawidłową odpowiedź

            if (selectedAnswerIndex == correctAnswerIndex) {
                score.value = (score.value ?: 0) + 1
            } else {
                newColors[selectedAnswerIndex] = wrongColor  // pokazujemy błędną odpowiedź
            }

            buttonColors.value = newColors

            // Czekamy chwilę przed pokazaniem następnego pytania
            delay(questionDelay)

            // Resetujemy kolory i pokazujemy następne pytanie
            currentQuestionIndex++
            buttonColors.value = List(4) { defaultColor }
            showNextQuestion()
        }
    }
}