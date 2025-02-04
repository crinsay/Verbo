package com.example.verbo.openquestion

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.verbo.R
import com.example.verbo.closequestion.CloseQuestionFragmentDirections
import com.example.verbo.databinding.FragmentOpenQuestionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OpenQuestionFragment : Fragment() {

    private val args: OpenQuestionFragmentArgs by navArgs()
    private lateinit var binding: FragmentOpenQuestionBinding
    private val viewModel: OpenQuestionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setDeckId(args.deckId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOpenQuestionBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.openQuestionViewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        setupObservers()
    }

    private fun setupClickListeners() {
        binding.nextWordButton.setOnClickListener {
            viewModel.checkAnswer()
        }

        binding.Powrot.setOnClickListener {
            val action = OpenQuestionFragmentDirections
                .actionOpenQuestionFragmentToStudyFragment(args.deckId)
            findNavController().navigate(action)
        }
    }

    private fun setupObservers() {
        viewModel.isTestFinished.observe(viewLifecycleOwner) { isFinished ->
            if (isFinished) {
                showTestResults()
            }
        }
    }

    private fun showTestResults() {
        Toast.makeText(
            context,
            "Koniec testu!",
            Toast.LENGTH_LONG
        ).show()

        val action = OpenQuestionFragmentDirections
            .actionOpenQuestionFragmentToStudyFragment(args.deckId)
        findNavController().navigate(action)
    }
}