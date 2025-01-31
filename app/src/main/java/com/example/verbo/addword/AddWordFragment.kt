package com.example.verbo.addword

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.verbo.R
import com.example.verbo.databinding.FragmentAddWordBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddWordFragment : Fragment() {

    private val args: AddWordFragmentArgs by navArgs()
    private val deckId by lazy { args.deckId }
    private lateinit var binding: FragmentAddWordBinding
    private val viewModel: AddWordViewModel by viewModels()

    companion object {
        fun newInstance() = AddWordFragment()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddWordBinding.inflate(inflater, container, false)
        binding.wordViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.nextWordButton.setOnClickListener {
            val question = binding.addWord.text.toString()
            val answer = binding.addWordTranslate.text.toString()

            if (question.isNotBlank() && answer.isNotBlank()) {
                viewModel.addFlashcard(deckId)
                Toast.makeText(context, "Fiszka dodana!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Proszę wypełnić oba pola", Toast.LENGTH_SHORT).show()
            }
        }

    }
}