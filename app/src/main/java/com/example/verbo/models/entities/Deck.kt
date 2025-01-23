package com.example.verbo.models.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.verbo.models.DatabaseConstants

@Entity(
    tableName = DatabaseConstants.TableNames.DECK, foreignKeys = [
    ForeignKey(
        entity = Language::class,
        parentColumns = ["languageId"],
        childColumns = ["languageId"],
        onDelete = ForeignKey.CASCADE
    )
],
    indices = [Index(value = ["deckId"], unique = true),
               Index(value = ["languageId"], unique = false)]
)
data class Deck(
    @PrimaryKey(autoGenerate = true)
    val deckId: Long,
    val name: String,
    var languageId: Long
)
