package com.example.verbo.models.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.verbo.models.DatabaseConstants

@Entity(
    tableName = DatabaseConstants.TableNames.FLASHCARD, foreignKeys = [
    ForeignKey(
        entity = Deck::class,
        parentColumns = ["deckId"],
        childColumns = ["deckId"],
        onDelete = ForeignKey.CASCADE
    )
],
    indices = [Index(value = ["flashcardId"], unique = true),
               Index(value = ["deckId"], unique = false)]
)
data class Flashcard(
    @PrimaryKey(autoGenerate = true)
    val flashcardId: Long,
    val wordDefinition: String,
    val wordTranslation: String,
    var deckId: Long
)
