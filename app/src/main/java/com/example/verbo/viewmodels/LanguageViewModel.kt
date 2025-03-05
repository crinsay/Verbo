package com.example.verbo.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verbo.common.dtos.LanguageDto
import com.example.verbo.models.repositories.languagerepository.ILanguageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LanguageViewModel @Inject constructor(
    private val languageRepository: ILanguageRepository
) : ViewModel() {
    val languageName = MutableLiveData<String>()
    private var originalLanguageName: String? = null

    val isSaveLanguageButtonEnabled = MediatorLiveData<Boolean>().apply {
        addSource(languageName) { checkFields() }
    }

    fun getLanguage(languageId: Long) {
        viewModelScope.launch {
            val language = languageRepository.getLanguageById(languageId)

            languageName.postValue(language.name)
            originalLanguageName = language.name
        }
    }

    suspend fun saveLanguage(languageId: Long) {
        val languageDto = LanguageDto(
            languageId = languageId,
            name = languageName.value!!.trim()
        )

        if (languageId != 0L) {
            languageRepository.updateLanguage(languageDto)
        }
        else {
            languageRepository.insertLanguage(languageDto)
        }
    }

    //Validate fields:
    private fun checkFields() {
        isSaveLanguageButtonEnabled.value = canSaveLanguage()
    }

    private fun canSaveLanguage(): Boolean {
        return languageName.value?.trim()?.isNotEmpty() == true
                && originalLanguageName != languageName.value!!.trim()
    }
}