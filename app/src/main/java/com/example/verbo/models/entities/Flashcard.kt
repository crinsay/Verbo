package com.example.verbo.models.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.verbo.models.DatabaseConstants

@Entity(
    tableName = DatabaseConstants.TablesNames.FLASHCARD, foreignKeys = [
    ForeignKey(
        entity = Set::class,
        parentColumns = ["setId"],
        childColumns = ["setId"],
        onDelete = ForeignKey.CASCADE
    )
],
    indices = [Index(value = ["flashcardId"], unique = true),
              Index(value = ["setId"], unique = false)]
)
data class Flashcard(
    @PrimaryKey(autoGenerate = true)
    val flashcardId: Long,
    val wordDefinition: String,
    val wordTranslation: String,
    val setId: Long
)
