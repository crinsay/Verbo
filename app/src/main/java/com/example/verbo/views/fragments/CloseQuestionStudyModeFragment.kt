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
import com.example.verbo.databinding.FragmentCloseQuestionStudyModeBinding
import com.example.verbo.viewmodels.CloseQuestionStudyModeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CloseQuestionStudyModeFragment : Fragment() {
    private val args: CloseQuestionStudyModeFragmentArgs by navArgs()
    private lateinit var binding: FragmentCloseQuestionStudyModeBinding
    private val viewModel: CloseQuestionStudyModeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.prepareFlashcards(args.deckId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCloseQuestionStudyModeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.closedQuestionViewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            answer1Button.setOnClickListener {
                viewModel.checkAnswerAndShowNextFlashcard(0)
            }

            answer2Button.setOnClickListener {
                viewModel.checkAnswerAndShowNextFlashcard(1)
            }

            answer3Button.setOnClickListener {
                viewModel.checkAnswerAndShowNextFlashcard(2)
            }

            answer4Button.setOnClickListener {
                viewModel.checkAnswerAndShowNextFlashcard(3)
            }

            cancelButton.setOnClickListener {
                val action = CloseQuestionStudyModeFragmentDirections.actionCloseQuestionFragmentToStudyFragment(args.deckId)
                findNavController().navigate(action)
            }
        }

        viewModel.isStudyFinished.observe(viewLifecycleOwner) { isFinished ->
            if (isFinished) {
                Toast.makeText(requireContext(), "Study ended", Toast.LENGTH_LONG).show()

                val action = CloseQuestionStudyModeFragmentDirections.actionCloseQuestionFragmentToStudyFragment(args.deckId)
                findNavController().navigate(action)
            }
        }
    }
}