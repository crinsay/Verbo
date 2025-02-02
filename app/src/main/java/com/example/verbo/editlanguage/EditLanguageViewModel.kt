package com.example.verbo.editlanguage

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verbo.common.dtos.LanguageDto
import com.example.verbo.models.repositories.languagerepository.ILanguageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditLanguageViewModel @Inject constructor(
    private val languageRepository: ILanguageRepository
) : ViewModel() {
    val languageName = MutableLiveData<String>()

    fun loadLanguage(languageId: Long) {
        viewModelScope.launch {
            try {
                val language = languageRepository.getLanguageById(languageId)
                languageName.value = language.name
            } catch (e: Exception) {
                Log.e("EditLanguageViewModel", "Błąd podczas ładowania języka", e)
            }
        }
    }

    suspend fun updateLanguage(languageId: Long) {
        languageName.value?.let { name ->
            if (name.isNotBlank()) {
                val languageDto = LanguageDto(
                    languageId = languageId,
                    name = name.trim()
                )
                try {
                    languageRepository.updateLanguage(languageDto)
                } catch (e: Exception) {
                    Log.e("EditLanguageViewModel", "Błąd podczas aktualizacji języka", e)
                }
            }
        }
    }
}