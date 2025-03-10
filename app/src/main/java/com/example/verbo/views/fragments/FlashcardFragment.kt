package com.example.verbo.views.fragments

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.verbo.databinding.FragmentFlashcardBinding
import com.example.verbo.viewmodels.FlashcardViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FlashcardFragment : Fragment() {
    private val args: FlashcardFragmentArgs by navArgs()
    private lateinit var binding: FragmentFlashcardBinding
    private val viewModel: FlashcardViewModel by viewModels()

    companion object {
        fun newInstance() = FlashcardFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (args.flashcardId != 0L) {
            viewModel.getFlashcardById(args.flashcardId)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFlashcardBinding.inflate(inflater, container, false)
        binding.wordViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            saveFlashcardButton.setOnClickListener {
                lifecycleScope.launch {
                    viewModel.saveFlashcard(args.flashcardId, args.deckId)

                    if (args.flashcardId != 0L) {
                        Toast.makeText(requireContext(), "Flashcard updated", Toast.LENGTH_SHORT).show()

                        val action = FlashcardFragmentDirections.actionAddWordFragmentToEditSetFragment(
                            deckId = args.deckId,
                            languageId = args.languageId
                        )
                        findNavController().navigate(action)
                    }
                    else {
                        Toast.makeText(requireContext(), "Flashcard added", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            cancelButton.setOnClickListener {
                val action = FlashcardFragmentDirections.actionAddWordFragmentToEditSetFragment(
                    deckId = args.deckId,
                    languageId = args.languageId
                )
                findNavController().navigate(action)
            }

            viewModel.isSaveFlashcardButtonEnabled.observe(viewLifecycleOwner) { state ->
                saveFlashcardButton.apply {
                    isEnabled = state
                    alpha = if (state) 1.0F else 0.5F
                }
            }
        }
    }
}