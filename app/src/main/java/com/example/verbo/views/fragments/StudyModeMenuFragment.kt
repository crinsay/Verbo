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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudyModeMenuBinding.inflate(inflater, container, false)

        binding.cancelButton.setOnClickListener {
            val action = StudyModeMenuFragmentDirections.actionStudyFragmentToSetsFragment()
            findNavController().navigate(action)
        }
        binding.closedQuestionsButton.setOnClickListener {
            val action = StudyModeMenuFragmentDirections.actionStudyFragmentToCloseQuestionFragment(
                args.deckId
            )
            findNavController().navigate(action)
        }
        binding.openQuestionsButton.setOnClickListener {
            val action = StudyModeMenuFragmentDirections.actionStudyFragmentToOpenQuestionFragment(
                args.deckId
            )
            findNavController().navigate(action)
        }
        binding.timeQuestionsButton.setOnClickListener {
            val action = StudyModeMenuFragmentDirections.actionStudyFragmentToTestQuestionFragment(
                args.deckId
            )
            findNavController().navigate(action)
        }

        return binding.root
    }
}
