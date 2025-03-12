package com.example.verbo.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verbo.models.repositories.languagerepository.ILanguageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainMenuViewModel @Inject constructor(
    private val languageRepository: ILanguageRepository
): ViewModel() {
    private val _canGoToDecks = MutableLiveData<Boolean>()
    val canGoToDecks: LiveData<Boolean> = _canGoToDecks

    fun checkIfAnyLanguageExist() {
        viewModelScope.launch {
            val isAnyLanguageExist = languageRepository.isAnyLanguageExist()

            if (!isAnyLanguageExist) {
                _canGoToDecks.postValue(false)
            }
            else {
                _canGoToDecks.postValue(true)
            }
        }
    }
}