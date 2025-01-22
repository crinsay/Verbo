package com.example.verbo.models.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.example.verbo.models.DatabaseConstants

@Entity(
    tableName = DatabaseConstants.TablesNames.LANGUAGE,
    indices = [Index(value = ["languageId"], unique = true)]
)
data class Language(
    val languageId: Long,
    val name: String
)
