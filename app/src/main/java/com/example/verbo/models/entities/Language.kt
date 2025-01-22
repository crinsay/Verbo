package com.example.verbo.models.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.verbo.models.DatabaseConstants

@Entity(
    tableName = DatabaseConstants.TableNames.LANGUAGE,
    indices = [Index(value = ["languageId"], unique = true)]
)
data class Language(
    @PrimaryKey(autoGenerate = true)
    val languageId: Long,
    val name: String
)
