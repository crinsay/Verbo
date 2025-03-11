package com.example.verbo.models.repositories.languagerepository

import com.example.verbo.common.dtos.LanguageDto
import com.example.verbo.common.mappers.mapToLanguage
import com.example.verbo.common.mappers.mapToLanguageDto
import com.example.verbo.common.mappers.mapToLanguageDtos
import com.example.verbo.models.daos.LanguageDao
import javax.inject.Inject

class LanguageRepository @Inject constructor(
    private val languageDao: LanguageDao
) : ILanguageRepository {
    override suspend fun insertLanguage(newLanguageDto: LanguageDto) {
        val newLanguage = newLanguageDto.mapToLanguage()
        languageDao.insertLanguage(newLanguage)
    }

    override suspend fun updateLanguage(updatedLanguageDto: LanguageDto) {
        val updatedLanguage = updatedLanguageDto.mapToLanguage()
        languageDao.updateLanguage(updatedLanguage)
    }

    override suspend fun deleteLanguage(languageToDeleteDto: LanguageDto) {
        val languageToDelete = languageToDeleteDto.mapToLanguage()
        languageDao.deleteLanguage(languageToDelete)
    }

    override suspend fun getAllLanguages(): List<LanguageDto> {
        val languages = languageDao.getAllLanguages()
        val languageDtos = languages.mapToLanguageDtos()

        return languageDtos
    }

    override suspend fun getLanguageById(languageId: Long): LanguageDto {
        val language = languageDao.getLanguageById(languageId)
        val languageDto = language.mapToLanguageDto()

        return languageDto
    }

    override suspend fun isAnyLanguageExist(): Boolean {
        val isAnyLanguageExist = languageDao.isAnyLanguageExist()
        return isAnyLanguageExist
    }
}