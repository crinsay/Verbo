package com.example.verbo.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verbo.common.dtos.DeckDto
import com.example.verbo.common.dtos.LanguageDto
import com.example.verbo.models.repositories.deckrepository.IDeckRepository
import com.example.verbo.models.repositories.languagerepository.ILanguageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddDeckViewModel @Inject constructor(
    private val languageRepository: ILanguageRepository,
    private val deckRepository: IDeckRepository
): ViewModel() {
    private val _languages = MutableStateFlow<List<LanguageDto>>(emptyList())
    val languages: StateFlow<List<LanguageDto>> = _languages

    val deckName = MutableLiveData<String>()
    var deckId = 0L

    var selectedLanguageId: Long = 0L

    val isSaveDeckButtonEnabled = MediatorLiveData<Boolean>().apply {
        addSource(deckName) { checkFields() }
    }

    fun getAllLanguages() {
        viewModelScope.launch {
            val languages = languageRepository.getAllLanguages()
            _languages.value = languages
        }
    }

    suspend fun saveDeck() {
        val deckDto = DeckDto(
            deckId = 0L,
            name = deckName.value!!.trim()
        )

        val newDeckId = deckRepository.insertDeck(deckDto, selectedLanguageId)
        deckId = newDeckId
    }

    //Validation:
    private fun checkFields() {
        isSaveDeckButtonEnabled.value = canSaveDeck()
    }

    private fun canSaveDeck(): Boolean {
        return deckName.value?.isNotBlank() == true
    }
}