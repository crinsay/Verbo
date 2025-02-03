package com.example.verbo.closequestion

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.verbo.R
import com.example.verbo.databinding.FragmentCloseQuestionBinding
import com.example.verbo.databinding.FragmentEditSetBinding
import com.example.verbo.editset.EditSetFragmentArgs
import com.example.verbo.editset.EditSetFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CloseQuestionFragment : Fragment() {

    private val args: CloseQuestionFragmentArgs by navArgs()
    private lateinit var binding: FragmentCloseQuestionBinding
    private val viewModel: CloseQuestionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setDeckId(args.deckId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCloseQuestionBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.closedQuestionViewModel = viewModel
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
                val action = CloseQuestionFragmentDirections
                    .actionCloseQuestionFragmentToStudyFragment()
                findNavController().navigate(action)
            }
        }
    }

    private fun showTestResults() {
        Toast.makeText(
            context,
            "Koniec Nauki!",
            Toast.LENGTH_LONG
        ).show()

        findNavController().navigateUp()
    }
}