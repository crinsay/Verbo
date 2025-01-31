package com.example.verbo.editset

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
import com.example.verbo.addset.AddSetFragmentArgs
import com.example.verbo.addset.AddSetViewModel
import com.example.verbo.databinding.FragmentAddSetBinding
import com.example.verbo.databinding.FragmentEditSetBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditSetFragment : Fragment() {

    private val args: EditSetFragmentArgs by navArgs()
    private lateinit var binding: FragmentEditSetBinding
    private val viewModel: EditSetViewModel by viewModels()

    companion object {
        fun newInstance() = EditSetFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setDeckId(args.deckId)
        viewModel.setLanguageId(args.languageId)
        viewModel.loadDeckData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditSetBinding.inflate(inflater, container, false)
        binding.editSetViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            saveNameButton.setOnClickListener {
                if (editSetViewModel != null) {
                    val newDeckName = editSetViewModel.deckName.toString().trim()
                    editSetViewModel.updateDeckName(newDeckName)
                    lifecycleScope.launch {
                        editSetViewModel.updateDeck()
                    }
                }
            }

            Powrot.setOnClickListener {
                lifecycleScope.launch {
                    viewModel.updateDeck()
                }
                val action = EditSetFragmentDirections.actionEditSetFragmentToSetsFragment()
                findNavController().navigate(action)
            }
        }
    }
}