package com.example.verbo.sets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verbo.common.dtos.DeckDto
import com.example.verbo.common.dtos.LanguageDto
import com.example.verbo.models.repositories.deckrepository.DeckRepository
import com.example.verbo.models.repositories.deckrepository.IDeckRepository
import com.example.verbo.models.repositories.languagerepository.ILanguageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetsViewModel @Inject constructor(
    private val deckRepository: IDeckRepository,
    private val languageRepository: ILanguageRepository
): ViewModel() {
    private val _decks = MutableStateFlow<MutableList<DeckDto>>(mutableListOf())
    val decks: StateFlow<MutableList<DeckDto>> = _decks


    private val _languages = MutableStateFlow<List<LanguageDto>>(emptyList())
    val languages: StateFlow<List<LanguageDto>> = _languages


    init {
        loadLanguages()
    }
    private fun loadLanguages() {
        viewModelScope.launch {
            _languages.value = languageRepository.getAllLanguages()
        }
    }
    fun loadDecksByLanguageId(languageId: Long) {
        viewModelScope.launch {
            val decks = deckRepository.getDecksByLanguageId(languageId)
            _decks.value = decks.toMutableList()
        }
    }
}