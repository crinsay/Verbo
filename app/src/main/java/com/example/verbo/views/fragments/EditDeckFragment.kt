package com.example.verbo.views.fragments

import android.annotation.SuppressLint
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.verbo.R
import com.example.verbo.adapters.recyclerview.FlashcardsRecyclerViewAdapter
import com.example.verbo.adapters.spinner.LanguagesSpinnerAdapter
import com.example.verbo.databinding.FragmentEditDeckBinding
import com.example.verbo.viewmodels.EditDeckViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditDeckFragment : Fragment() {
    private val args: EditDeckFragmentArgs by navArgs()
    private lateinit var binding: FragmentEditDeckBinding
    private val viewModel: EditDeckViewModel by viewModels()
    private lateinit var flashcardsAdapter: FlashcardsRecyclerViewAdapter
    private lateinit var languagesAdapter: LanguagesSpinnerAdapter

    companion object {
        fun newInstance() = EditDeckFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getAllLanguages()
        viewModel.getDeckById(args.deckId)
        viewModel.getFlashcardsByDeckId(args.deckId)

        if (args.languageId != 0L) {
            viewModel.setSelectedLanguageId(args.languageId)
            viewModel.originalLanguageId = args.languageId
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
                            viewModel.setSelectedLanguageId(languageId)
                        }

                        binding.languagesSpinner.apply {
                            adapter = languagesAdapter
                            onItemSelectedListener = languagesAdapter.onItemSelectedListener
                            isEnabled = false

                            val index = languages.indexOfFirst { it.languageId == viewModel.selectedLanguageId.value }
                            if (index != -1) {
                                setSelection(index)
                            }
                        }
                    }
                }
            }
        }

        flashcardsAdapter = FlashcardsRecyclerViewAdapter.create()
        flashcardsAdapter.onItemLongClickListener = { view, flashcard, position ->
            val popupMenu = PopupMenu(requireContext(), view)

            popupMenu.menuInflater
                .inflate(R.menu.menu_remove_flashcard, popupMenu.menu)
            popupMenu.gravity = Gravity.END

            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.delete_option -> {
                        lifecycleScope.launch {
                            viewModel.deleteFlashcard(flashcard)
                            flashcardsAdapter.itemRemoved(position)
                        }
                        true
                    }
                    R.id.edit_option -> {
                        val action = EditDeckFragmentDirections.actionEditSetFragmentToAddWordFragment(
                            languageId = viewModel.originalLanguageId,
                            deckId =  args.deckId,
                            flashcardId = flashcard.flashcardId
                        )
                        findNavController().navigate(action)
                        true
                    }
                    else -> false
                }

            }

            popupMenu.show()
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.flashcards.collect {
                    flashcardsAdapter.fillWithData(it.toMutableList())
                }
            }
        }

        binding = FragmentEditDeckBinding.inflate(inflater, container, false)
        binding.apply {
            editSetViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
            recyclerViewWords.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = flashcardsAdapter
            }
        }

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            editOrSaveDeckNameButton.setOnClickListener {
                if (viewModel.deckNameMode == EditDeckViewModel.DeckNameMode.IDLE) {
                    editOrSaveDeckNameButton.isEnabled = false
                    editOrSaveDeckNameButton.alpha = 0.5F
                    deckNameTextView.isEnabled = true
                    deckNameTextView.alpha = 1.0F
                    languagesSpinner.isEnabled = true

                    viewModel.deckNameMode = EditDeckViewModel.DeckNameMode.EDIT
                }
                else {
                    deckNameTextView.isEnabled = false
                    deckNameTextView.alpha = 0.75F
                    languagesSpinner.isEnabled = false
                    lifecycleScope.launch {
                        viewModel.saveDeck(args.deckId) //We are getting the selected languageId from viewModel.
                        Toast.makeText(requireContext(), "Saved changes", Toast.LENGTH_SHORT).show()
                    }

                    viewModel.deckNameMode = EditDeckViewModel.DeckNameMode.IDLE
                }

                editOrSaveDeckNameButton.text = viewModel.deckNameMode.relatedText
            }

            addWordButton.setOnClickListener{
                lifecycleScope.launch {
                    val action = EditDeckFragmentDirections.actionEditSetFragmentToAddWordFragment(
                        deckId = args.deckId,
                        languageId = viewModel.originalLanguageId
                    )
                    findNavController().navigate(action)
                }
            }

            Powrot.setOnClickListener {
                val action = EditDeckFragmentDirections.actionEditSetFragmentToSetsFragment(viewModel.originalLanguageId)
                findNavController().navigate(action)
            }

            viewModel.isSaveDeckButtonEnabled.observe(viewLifecycleOwner) { state ->
                if (viewModel.deckNameMode == EditDeckViewModel.DeckNameMode.EDIT) {
                    editOrSaveDeckNameButton.apply {
                        isEnabled = state
                        alpha = if (state) 1.0F else 0.5F
                    }
                }
            }
        }
    }
}