package com.example.verbo.views.fragments

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.verbo.databinding.FragmentEditFlashcardBinding
import com.example.verbo.viewmodels.EditFlashcardViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditFlashcardFragment : Fragment() {
    private val viewModel: EditFlashcardViewModel by viewModels()
    private val args: EditFlashcardFragmentArgs by navArgs()
    private lateinit var binding: FragmentEditFlashcardBinding

    companion object {
        fun newInstance() = EditFlashcardFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getFlashcardById(args.flashcardId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditFlashcardBinding.inflate(inflater, container, false)
        binding.editWordViewModel = viewModel //TODO: rename to editFlashcardViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            Zapisz.setOnClickListener {
                lifecycleScope.launch {
                    viewModel.updateFlashcard(args.flashcardId, args.deckId)

                    val action = EditFlashcardFragmentDirections.actionEditWordFragmentToEditSetFragment(
                        deckId = args.deckId,
                        languageId = args.languageId
                    )
                    findNavController().navigate(action)
                }
            }

            Anuluj.setOnClickListener{
                val action = EditFlashcardFragmentDirections.actionEditWordFragmentToEditSetFragment(
                    deckId = args.deckId,
                    languageId = args.languageId
                )
                findNavController().navigate(action)
            }

            viewModel.isSaveFlashcardButtonEnabled.observe(viewLifecycleOwner) { state ->
                Zapisz.apply {
                    isEnabled = state
                    alpha = if (state) 1.0F else 0.5F
                }
            }
        }
    }
}