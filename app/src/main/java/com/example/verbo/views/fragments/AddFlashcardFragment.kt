package com.example.verbo.views.fragments

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.verbo.databinding.FragmentAddFlashcardBinding
import com.example.verbo.viewmodels.AddFlashcardViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddFlashcardFragment : Fragment() {
    private val args: AddFlashcardFragmentArgs by navArgs()
    private lateinit var binding: FragmentAddFlashcardBinding
    private val viewModel: AddFlashcardViewModel by viewModels()

    companion object {
        fun newInstance() = AddFlashcardFragment()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddFlashcardBinding.inflate(inflater, container, false)
        binding.wordViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.nextWordButton.setOnClickListener {
            val question = binding.addWord.text.toString()
            val answer = binding.addWordTranslate.text.toString()
            Log.d("AddWordFragment", "deckId: ${args.deckId}, question: $question, answer: $answer")

            if (question.isNotBlank() && answer.isNotBlank()) {
                viewModel.addFlashcard(args.deckId)
                Toast.makeText(context, "Fiszka dodana!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Proszę wypełnić oba pola", Toast.LENGTH_SHORT).show()
            }
        }

        binding.exitButton.setOnClickListener {
            val action = AddFlashcardFragmentDirections.actionAddWordFragmentToEditSetFragment(
                deckId = args.deckId,
                languageId = args.languageId
            )
            findNavController().navigate(action)
        }
    }
}