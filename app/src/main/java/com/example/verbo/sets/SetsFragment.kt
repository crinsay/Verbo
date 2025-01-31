package com.example.verbo.sets

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.verbo.R
import com.example.verbo.adapters.LanguagesRecyclerViewAdapter
import com.example.verbo.adapters.SetRecyclerViewAdapter
import com.example.verbo.common.dtos.DeckDto
import com.example.verbo.databinding.FragmentLanguagesListBinding
import com.example.verbo.databinding.FragmentSetBinding
import com.example.verbo.databinding.FragmentSetsBinding
import com.example.verbo.languageslist.LanguagesListFragment
import com.example.verbo.languageslist.LanguagesListFragmentDirections
import com.example.verbo.languageslist.LanguagesListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import android.widget.Spinner
import android.widget.ArrayAdapter
import android.widget.AdapterView
import android.widget.PopupMenu
import com.example.verbo.addset.AddSetFragmentDirections

@AndroidEntryPoint
class SetsFragment : Fragment() {

    private lateinit var binding: FragmentSetsBinding
    private lateinit var setsAdapter: SetRecyclerViewAdapter
    private var selectedLanguageId: Long = -1

    private val viewModel: SetsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSetsBinding.inflate(inflater, container, false)

        setupRecyclerView()
        setupSpinner()

        return binding.root
    }

    private fun setupRecyclerView() {
        setsAdapter = SetRecyclerViewAdapter(mutableListOf())

        setsAdapter.onItemClickListener = { setId ->
            val action = SetsFragmentDirections.actionSetsFragmentToAddSetFragment(setId)
            findNavController().navigate(action)
        }
        setsAdapter.onItemLongClickListener = { view, set, position ->
            val popupMenu = PopupMenu(requireContext(), view)

            popupMenu.menuInflater
                .inflate(R.menu.menu_set_popup, popupMenu.menu)
            popupMenu.gravity = Gravity.END

            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.delete_option -> {
                        lifecycleScope.launch {
                            viewModel.deleteDeck(set)
                            setsAdapter.itemRemoved(position)
                        }
                        true
                    }
                    R.id.edit_option -> {
                        val action = SetsFragmentDirections.actionSetsFragmentToEditSetFragment(set.deckId)
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
            adapter = setsAdapter
        }

        // Obserwuj zmiany w zestawach
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.decks.collect {
                    setsAdapter.fillWithData(it.toMutableList())
                }
            }
        }
    }

    private fun setupSpinner() {
        // Obserwuj listę języków i aktualizuj spinner
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

                        // Ustaw listener dla spinneras
                        binding.spinnerLanguages.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                selectedLanguageId = languages[position].languageId
                                viewModel.loadDecksByLanguageId(selectedLanguageId)
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {
                                // Opcjonalnie możesz tu coś dodać
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            backButton.setOnClickListener {
                findNavController().navigate(R.id.action_setsFragment_to_menuFragment)
            }
            addSetButton.setOnClickListener {
                Log.d("SetsFragment", "Kliknięto dodaj zestaw, nawiguję do AddSetFragment")
                val action = SetsFragmentDirections
                    .actionSetsFragmentToAddSetFragment(selectedLanguageId)
                findNavController().navigate(action)
            }
        }
    }
}