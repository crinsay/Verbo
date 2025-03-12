package com.example.verbo.views.fragments

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.verbo.adapters.spinner.LanguagesSpinnerAdapter
import com.example.verbo.databinding.FragmentAddDeckBinding
import com.example.verbo.viewmodels.AddDeckViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddDeckFragment : Fragment() {
    private val args : AddDeckFragmentArgs by navArgs()
    private  lateinit var binding : FragmentAddDeckBinding
    private val viewModel: AddDeckViewModel by viewModels()
    private lateinit var languagesAdapter: LanguagesSpinnerAdapter

    companion object {
        fun newInstance() = AddDeckFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getAllLanguages()

        if (args.languageId != 0L) {
            viewModel.selectedLanguageId = args.languageId
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Setup languages spinner:
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.languages.collect { languages ->
                    if (languages.isNotEmpty()) {
                        languagesAdapter = LanguagesSpinnerAdapter.create(
                            requireContext(),
                            android.R.layout.simple_spinner_item,
                            languages
                        )
                        languagesAdapter.onItemClickListener = { languageId ->
                            viewModel.selectedLanguageId = languageId
                        }

                        binding.languagesSpinner.apply {
                            adapter = languagesAdapter
                            onItemSelectedListener = languagesAdapter.onItemSelectedListener

                            val index = languages.indexOfFirst { it.languageId == viewModel.selectedLanguageId }
                            if (index != -1) {
                                setSelection(index)
                            }
                        }
                    }
                }
            }
        }

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
                    viewModel.saveDeck() //We are getting selected languageId from viewModel directly.
                    val action = AddDeckFragmentDirections.actionAddSetFragmentToEditSetFragment(viewModel.deckId, viewModel.selectedLanguageId)
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