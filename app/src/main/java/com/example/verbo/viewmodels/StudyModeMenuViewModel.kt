package com.example.verbo.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verbo.models.repositories.deckrepository.DeckRepository
import com.example.verbo.models.repositories.deckrepository.IDeckRepository
import com.example.verbo.models.repositories.flashcardrepository.IFlashcardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudyModeMenuViewModel  @Inject constructor(
    private val flashcardRepository: IFlashcardRepository
): ViewModel() {
    private val _canStudy = MutableLiveData<Boolean>()
    val canStudy: LiveData<Boolean> = _canStudy

    fun checkDeckFlashcardsCount(deckId: Long) {
        viewModelScope.launch {
            val flashcardsCount = flashcardRepository.countFlashcardsByDeckId(deckId)

            if (flashcardsCount < 4) {
                _canStudy.postValue(false)
            }
            else {
                _canStudy.postValue(true)
            }
        }
    }
}