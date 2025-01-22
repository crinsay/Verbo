package com.example.verbo.models.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.verbo.models.DatabaseConstants

@Entity(
    tableName = DatabaseConstants.TablesNames.SET, foreignKeys = [
    ForeignKey(
        entity = Language::class,
        parentColumns = ["languageId"],
        childColumns = ["languageId"],
        onDelete = ForeignKey.CASCADE
    )
],
    indices = [Index(value = ["setId"], unique = true),
        Index(value = ["languageId"], unique = false)]
)
data class Set(
    @PrimaryKey(autoGenerate = true)
    val setId: Long,
    val name: String,
    val languageId: Long
)
