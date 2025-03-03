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

class MainMenuFragment : Fragment() {
    private lateinit var binding: FragmentMainMenuBinding
    private val viewModel: MainMenuViewModel by viewModels()

    companion object {
        fun newInstance() = MainMenuFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainMenuBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

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
                val action = MainMenuFragmentDirections.actionMenuFragmentToSetsFragment()
                findNavController().navigate(action)
            }
        }
    }
}