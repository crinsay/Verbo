package com.example.verbo.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verbo.common.dtos.LanguageDto
import com.example.verbo.models.repositories.languagerepository.ILanguageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LanguagesViewModel @Inject constructor(
    private val languageRepository: ILanguageRepository
) : ViewModel() {
    private val _languages = MutableStateFlow<MutableList<LanguageDto>>(mutableListOf())
    val languages: StateFlow<MutableList<LanguageDto>> = _languages

    fun loadAllLanguages() {
        viewModelScope.launch {
            val languages = languageRepository.getAllLanguages()
            _languages.value = languages.toMutableList()
        }
    }

    suspend fun deleteLanguage(LanguageToDelteDto: LanguageDto){
        languageRepository.deleteLanguage(LanguageToDelteDto)
        _languages.value.remove(LanguageToDelteDto)
    }
}