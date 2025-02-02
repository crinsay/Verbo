package com.example.verbo.study

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.verbo.databinding.FragmentStudyBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StudyFragment : Fragment() {

    private lateinit var binding: FragmentStudyBinding
    private val args: StudyFragmentArgs by navArgs()
    private val viewModel: StudyViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudyBinding.inflate(inflater, container, false)

        binding.cancelButton.setOnClickListener {
            val action = StudyFragmentDirections.actionStudyFragmentToSetsFragment()
            findNavController().navigate(action)
        }
        binding.closedQuestionsButton.setOnClickListener {
            val action = StudyFragmentDirections.actionStudyFragmentToCloseQuestionFragment(
                args.languageId, args.deckId
            )
            findNavController().navigate(action)
        }
        binding.openQuestionsButton.setOnClickListener {
            val action = StudyFragmentDirections.actionStudyFragmentToOpenQuestionFragment(
                args.languageId, args.deckId
            )
            findNavController().navigate(action)
        }

        return binding.root
    }
}
