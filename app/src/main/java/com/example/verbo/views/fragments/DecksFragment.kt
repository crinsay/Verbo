package com.example.verbo.views.fragments

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
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
import com.example.verbo.adapters.DecksRecyclerViewAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import android.widget.ArrayAdapter
import android.widget.AdapterView
import android.widget.PopupMenu
import com.example.verbo.databinding.FragmentDecksBinding
import com.example.verbo.viewmodels.DecksViewModel

@AndroidEntryPoint
class DecksFragment : Fragment() {
    private lateinit var binding: FragmentDecksBinding
    private lateinit var decksAdapter: DecksRecyclerViewAdapter
    private val viewModel: DecksViewModel by viewModels()
    private var selectedLanguageId: Long = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDecksBinding.inflate(inflater, container, false)

        setupRecyclerView()
        setupSpinner()
        return binding.root
    }

    private fun setupRecyclerView() {
        decksAdapter = DecksRecyclerViewAdapter(mutableListOf())

        decksAdapter.onItemClickListener = { setId ->
            val action = DecksFragmentDirections.actionSetsFragmentToStudyFragment(setId, selectedLanguageId)
            findNavController().navigate(action)
        }
        decksAdapter.onItemLongClickListener = { view, set, position ->
            val popupMenu = PopupMenu(requireContext(), view)

            popupMenu.menuInflater
                .inflate(R.menu.menu_set_popup, popupMenu.menu)
            popupMenu.gravity = Gravity.END

            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.delete_option -> {
                        lifecycleScope.launch {
                            viewModel.deleteDeck(set)
                            decksAdapter.itemRemoved(position)
                        }
                        true
                    }
                    R.id.edit_option -> {
                        val action = DecksFragmentDirections.actionSetsFragmentToEditSetFragment(set.deckId, selectedLanguageId)
                        findNavController().navigate(action)
                        true
                    }
                    else -> false
                }
            }

            popupMenu.show()
        }

        binding.recyclerViewSets.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = decksAdapter
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.decks.collect {
                    decksAdapter.fillWithData(it.toMutableList())
                }
            }
        }
    }

    private fun setupSpinner() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.languages.collect { languages ->
                    if (languages.isNotEmpty()) {
                        val adapter = ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_spinner_item,
                            languages.map { it.name }
                        ).apply {
                            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        }

                        binding.spinnerLanguages.adapter = adapter
                        binding.spinnerLanguages.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                selectedLanguageId = languages[position].languageId
                                viewModel.loadDecksByLanguageId(selectedLanguageId)
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {

                            }
                        }
                    }
                }
            }
        }
    }
    override fun onResume() {
        super.onResume()
        viewModel.refreshDecks(selectedLanguageId)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            backButton.setOnClickListener {
                findNavController().navigate(R.id.action_setsFragment_to_menuFragment)
            }
            addSetButton.setOnClickListener {
                Log.d("SetsFragment", "Kliknięto dodaj zestaw, nawiguję do AddSetFragment")
                val action = DecksFragmentDirections
                    .actionSetsFragmentToAddSetFragment(selectedLanguageId)
                findNavController().navigate(action)
            }
        }
    }
}