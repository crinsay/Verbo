package com.example.verbo.editset

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
import com.example.verbo.adapters.LanguagesRecyclerViewAdapter
import com.example.verbo.adapters.WordsRecyclerViewAdapter
import com.example.verbo.addset.AddSetFragmentArgs
import com.example.verbo.addset.AddSetViewModel
import com.example.verbo.databinding.FragmentAddSetBinding
import com.example.verbo.databinding.FragmentEditSetBinding
import com.example.verbo.languageslist.LanguagesListFragmentDirections
import com.example.verbo.sets.SetsFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditSetFragment : Fragment() {

    private val args: EditSetFragmentArgs by navArgs()
    private lateinit var binding: FragmentEditSetBinding
    private val viewModel: EditSetViewModel by viewModels()
    private lateinit var wordAdapter: WordsRecyclerViewAdapter

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

        wordAdapter = WordsRecyclerViewAdapter(mutableListOf())

        wordAdapter.onItemClickListener = { //pozniej przejscie do testu
        }

        wordAdapter.onItemLongClickListener = { view, word, position ->
            val popupMenu = PopupMenu(requireContext(), view)

            popupMenu.menuInflater
                .inflate(R.menu.menu_remove_flashcard, popupMenu.menu)
            popupMenu.gravity = Gravity.END


            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.delete_option -> {
                        lifecycleScope.launch {
                            viewModel.deleteWord(word)
                            wordAdapter.itemRemoved(position)
                        }
                        true
                    }
                    R.id.edit_option -> {
                        val flashcardId = word.flashcardId
                        val action = EditSetFragmentDirections.actionEditSetFragmentToEditWordFragment(flashcardId)
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
                viewModel.deckName.observe(viewLifecycleOwner) { name ->
                    binding.textViewSetName.setText(name)
                }
            }
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.flashes.collect {
                    wordAdapter.fillWithData(it.toMutableList())
                }
            }
        }

        binding = FragmentEditSetBinding.inflate(inflater, container, false)
        binding.apply {
            recyclerViewWords.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = wordAdapter
            }
        }
        /* Moze potrzebne nie wiem
        binding.editSetViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
         */
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            saveNameButton.setOnClickListener {
                val newDeckName = textViewSetName.text.toString().trim()
                viewModel.updateDeckName(newDeckName)
                viewLifecycleOwner.lifecycleScope.launch {
                    //viewModel.updateDeck()
                    Toast.makeText(requireContext(), "Zapisano zmiany", Toast.LENGTH_SHORT).show()
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