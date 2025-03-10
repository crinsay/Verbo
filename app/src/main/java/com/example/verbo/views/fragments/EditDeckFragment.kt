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

    companion object {
        fun newInstance() = EditDeckFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getDeckById(args.deckId)
        viewModel.getFlashcardsByDeckId(args.deckId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
                            languageId = args.languageId,
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

                    viewModel.deckNameMode = EditDeckViewModel.DeckNameMode.EDIT
                }
                else {
                    deckNameTextView.isEnabled = false
                    deckNameTextView.alpha = 0.75F
                    lifecycleScope.launch {
                        viewModel.saveDeck(args.deckId, args.languageId)
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
                        languageId = args.languageId
                    )
                    findNavController().navigate(action)
                }
            }

            Powrot.setOnClickListener {
                val action = EditDeckFragmentDirections.actionEditSetFragmentToSetsFragment(args.languageId)
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