package com.example.verbo.views.fragments

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.verbo.databinding.FragmentAddDeckBinding
import com.example.verbo.viewmodels.AddDeckViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddDeckFragment : Fragment() {
    private val args : AddDeckFragmentArgs by navArgs()
    private  lateinit var binding : FragmentAddDeckBinding
    private val viewModel: AddDeckViewModel by viewModels()

    companion object {
        fun newInstance() = AddDeckFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddDeckBinding.inflate(inflater, container, false)
        binding.setViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            saveDeckButton.setOnClickListener{
                lifecycleScope.launch {
                    viewModel.saveDeck(args.languageId)
                    val action = AddDeckFragmentDirections.actionAddSetFragmentToEditSetFragment(viewModel.deckId, args.languageId)
                    findNavController().navigate(action)
                }
            }

            cancelSetButton.setOnClickListener {
                val action = AddDeckFragmentDirections.actionAddSetFragmentToSetsFragment(args.languageId)
                findNavController().navigate(action)
            }

            viewModel.isSaveDeckButtonEnabled.observe(viewLifecycleOwner) { state ->
                saveDeckButton.apply {
                    isEnabled = state
                    alpha = if (state) 1.0F else 0.5F
                }
            }
        }
    }
}