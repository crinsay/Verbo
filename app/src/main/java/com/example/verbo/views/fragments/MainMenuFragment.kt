package com.example.verbo.views.fragments

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import androidx.room.ForeignKey
import com.example.verbo.R
import com.example.verbo.databinding.FragmentMainMenuBinding
import com.example.verbo.databinding.FragmentOpenQuestionStudyModeBinding
import com.example.verbo.viewmodels.MainMenuViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainMenuFragment : Fragment() {
    private val viewModel: MainMenuViewModel by viewModels()
    private lateinit var binding: FragmentMainMenuBinding

    companion object {
        fun newInstance() = MainMenuFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.checkIfAnyLanguageExist()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainMenuBinding.inflate(inflater, container, false)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            Jezyki.setOnClickListener {
                val action = MainMenuFragmentDirections.actionMenuFragmentToLanguagesListFragment()
                findNavController().navigate(action)
            }

            Zestawy.setOnClickListener {
                val action = MainMenuFragmentDirections.actionMenuFragmentToSetsFragment(0L)
                findNavController().navigate(action)
            }

            viewModel.canGoToDecks.observe(viewLifecycleOwner) { state ->
                Zestawy.apply {
                    isEnabled = state
                    alpha = if (state) 1.0F else 0.5F
                }

                if (!state) {
                    cannotGoToDecksTextView.visibility = View.VISIBLE
                }
            }
        }
    }
}