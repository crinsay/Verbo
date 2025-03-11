package com.example.verbo.viewmodels

import android.graphics.Color
import android.util.Log
import androidx.lifecycle.LiveData
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
class TestYourselfStudyModeViewModel @Inject constructor(
    private val flashcardRepository: IFlashcardRepository
) : ViewModel() {
    private val flashcards: MutableList<FlashcardDto> = mutableListOf()
    private val _flashcardsCount = MutableLiveData<Int>()
    val flashcardsCount: LiveData<Int> = _flashcardsCount

    private val _currentFlashcardNumber = MutableLiveData(0)
    val currentFlashcardNumber: LiveData<Int> = _currentFlashcardNumber

    private var currentCorrectAnswerIndex = 0
    var score = 0

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

    private val _isTestFinished = MutableLiveData(false)
    val isTestFinished: LiveData<Boolean> = _isTestFinished

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
            _flashcardsCount.value = flashcards.count()

            showNextFlashcard()
        }
    }

    private fun showNextFlashcard() {
        if (isStudyFinished()) {
            _isTestFinished.value = true
            return
        }

        val currentFlashcard = flashcards[_currentFlashcardNumber.value!! - 1]
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
            else {
                ++score
            }

            _answerButtonsColors.value = newColors

            //Wait a little bit:
            delay(1500L)

            _answerButtonsColors.value = List(4) { defaultColor }
            showNextFlashcard()
        }
    }

    private fun isStudyFinished(): Boolean {
        _currentFlashcardNumber.value = (_currentFlashcardNumber.value ?: 0) + 1
        return _currentFlashcardNumber.value!! > flashcards.size
    }
}