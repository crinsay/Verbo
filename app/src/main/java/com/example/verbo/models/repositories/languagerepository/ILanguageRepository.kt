package com.example.verbo.models.repositories.languagerepository

import com.example.verbo.common.dtos.LanguageDto

interface ILanguageRepository {
    suspend fun insertLanguage(newLanguageDto: LanguageDto)
    suspend fun updateLanguage(updatedLanguageDto: LanguageDto)
    suspend fun deleteLanguage(languageToDeleteDto: LanguageDto)
    suspend fun getAllLanguages(): List<LanguageDto>
    suspend fun getLanguageById(languageId: Long): LanguageDto
}