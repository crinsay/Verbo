package com.example.verbo.viewmodels

import android.graphics.Color
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verbo.common.dtos.FlashcardDto
import com.example.verbo.models.entities.Flashcard
import com.example.verbo.models.repositories.flashcardrepository.IFlashcardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CloseQuestionStudyModeViewModel @Inject constructor(
    private val flashcardRepository: IFlashcardRepository
) : ViewModel() {
    private val flashcards: MutableList<FlashcardDto> = mutableListOf()

    private var currentFlashcardNumber = 0
    private var currentCorrectAnswerIndex = 0

    private val _question = MutableLiveData<String>()
    val question: LiveData<String> = _question

    private val _answer1 = MutableLiveData<String>()
    val answer1: LiveData<String> = _answer1

    private val _answer2 = MutableLiveData<String>()
    val answer2: LiveData<String> = _answer2

    private val _answer3 = MutableLiveData<String>()
    val answer3: LiveData<String> = _answer3

    private val _answer4 = MutableLiveData<String>()
    val answer4: LiveData<String> = _answer4

    private val _isStudyFinished = MutableLiveData(false)
    val isStudyFinished: LiveData<Boolean> = _isStudyFinished

    private val _answerButtonsColors = MutableLiveData(List(4) { Color.parseColor("#e0da2f") })
    val answerButtonsColors: LiveData<List<Int>> = _answerButtonsColors
    private val defaultColor = Color.parseColor("#e0da2f")
    private val correctColor = Color.parseColor("#4CAF50")
    private val incorrectColor = Color.parseColor("#F44336")

    private val _canChooseAnswer = MutableLiveData(false)
    val canChooseAnswer: LiveData<Boolean> = _canChooseAnswer


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
        val incorrectRandomAnswers = getRandomIncorrectAnswers(currentFlashcard)
        val allRandomAnswers = (incorrectRandomAnswers + currentFlashcard.wordTranslation).shuffled()

        currentCorrectAnswerIndex = allRandomAnswers.indexOf(currentFlashcard.wordTranslation)

        _question.value = currentFlashcard.wordDefinition
        _answer1.value = allRandomAnswers[0]
        _answer2.value = allRandomAnswers[1]
        _answer3.value = allRandomAnswers[2]
        _answer4.value = allRandomAnswers[3]

        _canChooseAnswer.value = true
    }

    private fun getRandomIncorrectAnswers(currentFlashcard: FlashcardDto): List<String> {
        val incorrectAnswers = flashcards.filter { it.flashcardId != currentFlashcard.flashcardId }
            .map { it.wordTranslation }
            .shuffled()
            .take(3)

        return incorrectAnswers
    }

    fun checkAnswerAndShowNextFlashcard(selectedAnswerIndex: Int) {
        viewModelScope.launch {
            _canChooseAnswer.value = false

            val newColors = MutableList(4) { defaultColor }
            newColors[currentCorrectAnswerIndex] = correctColor

            if (selectedAnswerIndex != currentCorrectAnswerIndex) {
                newColors[selectedAnswerIndex] = incorrectColor
            }

            _answerButtonsColors.value = newColors

            //Wait a little bit:
            delay(1500L)

            _answerButtonsColors.value = List(4) { defaultColor }
            showNextFlashcard()
        }
    }

    private fun isStudyFinished(): Boolean {
        return ++currentFlashcardNumber > flashcards.size
    }
}