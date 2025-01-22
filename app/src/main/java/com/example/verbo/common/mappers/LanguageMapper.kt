package com.example.verbo.common.mappers

import com.example.verbo.common.dtos.LanguageDto
import com.example.verbo.models.entities.Language

fun Language.mapToLanguageDto(): LanguageDto {
    return LanguageDto(
        languageId = languageId,
        name = name
    )
}

fun List<Language>.mapToLanguageDtos(): List<LanguageDto> {
    return this.map {
        it.mapToLanguageDto()
    }
}

fun LanguageDto.mapToLanguage(): Language {
    return Language(
        languageId = languageId,
        name = name
    )
}