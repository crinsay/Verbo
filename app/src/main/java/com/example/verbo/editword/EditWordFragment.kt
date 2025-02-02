package com.example.verbo.editword

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.verbo.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditWordFragment : Fragment() {

    companion object {
        fun newInstance() = EditWordFragment()
    }

    private val viewModel: EditWordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_edit_word, container, false)
    }
}