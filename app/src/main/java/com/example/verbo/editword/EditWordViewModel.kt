package com.example.verbo.editword

import androidx.lifecycle.ViewModel
import com.example.verbo.models.repositories.flashcardrepository.IFlashcardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditWordViewModel @Inject constructor(
    private val flashcardRepository: IFlashcardRepository
): ViewModel() {
    // TODO: Implement the ViewModel
}