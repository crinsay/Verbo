package com.example.verbo.views.fragments

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.verbo.R
import com.example.verbo.adapters.recyclerview.DecksRecyclerViewAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import android.widget.ArrayAdapter
import android.widget.AdapterView
import android.widget.PopupMenu
import androidx.navigation.fragment.navArgs
import com.example.verbo.adapters.spinner.LanguagesSpinnerAdapter
import com.example.verbo.databinding.FragmentDecksBinding
import com.example.verbo.viewmodels.DecksViewModel

@AndroidEntryPoint
class DecksFragment : Fragment() {
    private lateinit var binding: FragmentDecksBinding
    private val args: DecksFragmentArgs by navArgs()
    private lateinit var languagesAdapter: LanguagesSpinnerAdapter
    private lateinit var decksAdapter: DecksRecyclerViewAdapter
    private val viewModel: DecksViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getAllLanguages()

        if (args.selectedLanguageId != 0L) {
            viewModel.selectedLanguageId = args.selectedLanguageId
            viewModel.getDecksByLanguageId() //We are getting languageId directly from ViewModel, so we don't need to pass it.
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
                            viewModel.getDecksByLanguageId() //We are getting languageId directly from ViewModel, so we don't need to pass it.
                        }

                        binding.spinnerLanguages.apply {
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

        //Setup decks recycler view and binding:
        decksAdapter = DecksRecyclerViewAdapter(mutableListOf())
        decksAdapter.onItemClickListener = { deckId ->
            val action = DecksFragmentDirections.actionSetsFragmentToStudyFragment(deckId, viewModel.selectedLanguageId)
            findNavController().navigate(action)
        }
        decksAdapter.onItemLongClickListener = { view, deck, position ->
            val popupMenu = PopupMenu(requireContext(), view)

            popupMenu.menuInflater
                .inflate(R.menu.menu_set_popup, popupMenu.menu)
            popupMenu.gravity = Gravity.END

            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.edit_option -> {
                        val action = DecksFragmentDirections.actionSetsFragmentToEditSetFragment(
                            deckId = deck.deckId,
                            languageId = viewModel.selectedLanguageId
                        )
                        findNavController().navigate(action)
                        true
                    }
                    R.id.delete_option -> {
                        lifecycleScope.launch {
                            viewModel.deleteDeck(deck)
                            decksAdapter.itemRemoved(position)
                        }
                        true
                    }
                    else -> false
                }
            }

            popupMenu.show()
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.decks.collect {
                    decksAdapter.fillWithData(it.toMutableList())
                }
            }
        }

        binding = FragmentDecksBinding.inflate(inflater, container, false)
        binding.recyclerViewSets.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = decksAdapter
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            backButton.setOnClickListener {
                val action = DecksFragmentDirections.actionSetsFragmentToMenuFragment()
                findNavController().navigate(action)
            }

            addSetButton.setOnClickListener {
                val action = DecksFragmentDirections.actionSetsFragmentToAddSetFragment(viewModel.selectedLanguageId)
                findNavController().navigate(action)
            }
        }
    }
}