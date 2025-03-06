package com.example.verbo.viewmodels

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
class DecksViewModel @Inject constructor(
    private val deckRepository: IDeckRepository,
    private val languageRepository: ILanguageRepository
): ViewModel() {
    private val _languages = MutableStateFlow<List<LanguageDto>>(emptyList())
    val languages: StateFlow<List<LanguageDto>> = _languages

    private val _decks = MutableStateFlow<MutableList<DeckDto>>(mutableListOf())
    val decks: StateFlow<MutableList<DeckDto>> = _decks

    var selectedLanguageId: Long = 0L


    fun getAllLanguages() {
        viewModelScope.launch {
            val languages = languageRepository.getAllLanguages()
            _languages.value = languages
        }
    }

    fun getDecksByLanguageId() {
        viewModelScope.launch {
            val decks = deckRepository.getDecksByLanguageId(selectedLanguageId)
            _decks.value = decks.toMutableList()
        }
    }

    suspend fun deleteDeck(deckToDeleteDto: DeckDto){
        deckRepository.deleteDeck(deckToDeleteDto)
        _decks.value.remove(deckToDeleteDto)
    }
}