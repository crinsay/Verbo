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
import com.example.verbo.databinding.FragmentTestYourselfStudyModeBinding
import com.example.verbo.viewmodels.TestYourselfStudyModeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TestYourselfStudyModeFragment : Fragment() {
    private val viewModel: TestYourselfStudyModeViewModel by viewModels()
    private val args: TestYourselfStudyModeFragmentArgs by navArgs()
    private lateinit var binding: FragmentTestYourselfStudyModeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.prepareFlashcards(args.deckId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTestYourselfStudyModeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.testQuestionViewModel = viewModel

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

        viewModel.isTestFinished.observe(viewLifecycleOwner) { isFinished ->
            if (isFinished) {
                val score = viewModel.score
                val flashcardsCount = viewModel.flashcardsCount.value
                Toast.makeText(requireContext(), "Test ended. Your score is: $score/$flashcardsCount", Toast.LENGTH_LONG).show()

                val action = TestYourselfStudyModeFragmentDirections.actionTestQuestionFragmentToStudyFragment(args.deckId)
                findNavController().navigate(action)
            }
        }
    }
}