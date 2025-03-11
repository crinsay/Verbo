package com.example.verbo.views.fragments

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.verbo.databinding.FragmentOpenQuestionStudyModeBinding
import com.example.verbo.viewmodels.OpenQuestionStudyModeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OpenQuestionStudyModeFragment : Fragment() {
    private val viewModel: OpenQuestionStudyModeViewModel by viewModels()
    private val args: OpenQuestionStudyModeFragmentArgs by navArgs()
    private lateinit var binding: FragmentOpenQuestionStudyModeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.prepareFlashcards(args.deckId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOpenQuestionStudyModeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.openQuestionViewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            nextWordButton.setOnClickListener {
                viewModel.checkAnswerAndShowNextFlashcard()
            }

            Powrot.setOnClickListener {
                val action = OpenQuestionStudyModeFragmentDirections.actionOpenQuestionFragmentToStudyFragment(args.deckId)
                findNavController().navigate(action)
            }

            viewModel.isNextFlashcardButtonEnabled.observe(viewLifecycleOwner) { state ->
                nextWordButton.apply {
                    isEnabled = state
                    alpha = if (state) 1.0F else 0.5F
                }
            }
        }

        viewModel.isStudyFinished.observe(viewLifecycleOwner) { isFinished ->
            if (isFinished) {
                Toast.makeText(requireContext(), "Study ended", Toast.LENGTH_LONG).show()

                val action = OpenQuestionStudyModeFragmentDirections.actionOpenQuestionFragmentToStudyFragment(args.deckId)
                findNavController().navigate(action)
            }
        }
    }
}