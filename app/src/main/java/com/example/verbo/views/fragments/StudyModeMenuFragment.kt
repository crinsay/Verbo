package com.example.verbo.views.fragments

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.verbo.databinding.FragmentStudyModeMenuBinding
import com.example.verbo.viewmodels.StudyModeMenuViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StudyModeMenuFragment : Fragment() {

    private lateinit var binding: FragmentStudyModeMenuBinding
    private val args: StudyModeMenuFragmentArgs by navArgs()
    private val viewModel: StudyModeMenuViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudyModeMenuBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            cancelButton.setOnClickListener {
                val action = StudyModeMenuFragmentDirections.actionStudyFragmentToSetsFragment(args.languageId)
                findNavController().navigate(action)
            }

            closedQuestionsButton.setOnClickListener {
                val action = StudyModeMenuFragmentDirections.actionStudyFragmentToCloseQuestionFragment(args.deckId)
                findNavController().navigate(action)
            }

            openQuestionsButton.setOnClickListener {
                val action = StudyModeMenuFragmentDirections.actionStudyFragmentToOpenQuestionFragment(args.deckId)
                findNavController().navigate(action)
            }

            timeQuestionsButton.setOnClickListener {
                val action = StudyModeMenuFragmentDirections.actionStudyFragmentToTestQuestionFragment(args.deckId)
                findNavController().navigate(action)
            }

            viewModel.canStudy.observe(viewLifecycleOwner) { state ->
                closedQuestionsButton.apply {
                    isEnabled = state
                    alpha = if (state) 1.0F else 0.5F
                }

                openQuestionsButton.apply {
                    isEnabled = state
                    alpha = if (state) 1.0F else 0.5F
                }

                timeQuestionsButton.apply {
                    isEnabled = state
                    alpha = if (state) 1.0F else 0.5F
                }

                if (!state) {
                    cannotStudyTextView.visibility = View.VISIBLE
                }
            }

            viewModel.checkDeckFlashcardsCount(args.deckId)
        }
    }
}
