package com.example.verbo.views.fragments

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.verbo.databinding.FragmentEditFlashcardBinding
import com.example.verbo.viewmodels.EditFlashcardViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditFlashcardFragment : Fragment() {

    private val args: EditFlashcardFragmentArgs by navArgs()
    private lateinit var binding: FragmentEditFlashcardBinding

    companion object {
        fun newInstance() = EditFlashcardFragment()
    }

    private val viewModel: EditFlashcardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setDeckId(args.deckId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditFlashcardBinding.inflate(inflater, container, false)
        binding.editWordViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadFlashcard(args.flashcardId)

        binding.Zapisz.setOnClickListener {
            viewModel.updateFlashcard(args.flashcardId)
            val action = EditFlashcardFragmentDirections.actionEditWordFragmentToEditSetFragment(
                deckId = args.deckId,
                languageId = args.languageId
            )
            findNavController().navigate(action)
        }

        binding.Anuluj.setOnClickListener{
            val action = EditFlashcardFragmentDirections.actionEditWordFragmentToEditSetFragment(
                deckId = args.deckId,
                languageId = args.languageId
            )
            findNavController().navigate(action)
        }
    }
}