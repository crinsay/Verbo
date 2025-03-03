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

    private val args: TestYourselfStudyModeFragmentArgs by navArgs()
    private lateinit var binding: FragmentTestYourselfStudyModeBinding
    private val viewModel: TestYourselfStudyModeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setDeckId(args.deckId)
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

        setupObservers()
        setupClickListeners()
    }

    private fun setupObservers() {
        viewModel.question.observe(viewLifecycleOwner) { question ->
            binding.questionViewWord.text = question
        }

        viewModel.answer1.observe(viewLifecycleOwner) { answer ->
            binding.answer1Button.text = answer
        }

        viewModel.answer2.observe(viewLifecycleOwner) { answer ->
            binding.answer2Button.text = answer
        }

        viewModel.answer3.observe(viewLifecycleOwner) { answer ->
            binding.answer3Button.text = answer
        }

        viewModel.answer4.observe(viewLifecycleOwner) { answer ->
            binding.answer4Button.text = answer
        }


        viewModel.isTestFinished.observe(viewLifecycleOwner) { isFinished ->
            if (isFinished) {
                showTestResults()
            }
        }
    }

    private fun setupClickListeners() {
        binding.apply {
            answer1Button.setOnClickListener { viewModel.checkAnswer(0) }
            answer2Button.setOnClickListener { viewModel.checkAnswer(1) }
            answer3Button.setOnClickListener { viewModel.checkAnswer(2) }
            answer4Button.setOnClickListener { viewModel.checkAnswer(3) }

            cancelButton.setOnClickListener {
                val action = TestYourselfStudyModeFragmentDirections
                    .actionTestQuestionFragmentToStudyFragment(args.deckId)
                findNavController().navigate(action)
            }
        }
    }

    private fun showTestResults() {
        val score = viewModel.score.value ?: 0
        val total = viewModel.currentQuestionIndex
        Toast.makeText(
            context,
            "Test zakończony! Wynik: $score/$total",
            Toast.LENGTH_LONG
        ).show()

        val action = TestYourselfStudyModeFragmentDirections
            .actionTestQuestionFragmentToStudyFragment(args.deckId)
        findNavController().navigate(action)
    }
}