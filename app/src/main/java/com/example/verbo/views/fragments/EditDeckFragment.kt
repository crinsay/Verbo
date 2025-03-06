package com.example.verbo.views.fragments

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
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
    private lateinit var wordAdapter: FlashcardsRecyclerViewAdapter

    companion object {
        fun newInstance() = EditDeckFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("EditSetFragment", "Otrzymane argumenty: deckId=${args.deckId}, languageId=${args.languageId}")

        viewModel.setDeckId(args.deckId)
        viewModel.setLanguageId(args.languageId)
        viewModel.loadDeckData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        wordAdapter = FlashcardsRecyclerViewAdapter(mutableListOf())

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
                            if (position >= 0) {
                                wordAdapter.itemRemoved(position)
                            }
                        }
                        true
                    }
                    R.id.edit_option -> {
                        val flashcardId = word.flashcardId
                        val action = EditDeckFragmentDirections.actionEditSetFragmentToEditWordFragment(flashcardId, args.deckId)
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

        binding = FragmentEditDeckBinding.inflate(inflater, container, false)
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
                val newDeckName = binding.textViewSetName.text.toString().trim()
                viewModel.updateDeckName(newDeckName)
                Toast.makeText(requireContext(), "Zapisano zmiany", Toast.LENGTH_SHORT).show()
            }
            addWordButton.setOnClickListener{
                lifecycleScope.launch {
                    val action = EditDeckFragmentDirections.actionEditSetFragmentToAddWordFragment(args.deckId)
                    findNavController().navigate(action)
                }
            }
            Powrot.setOnClickListener {
                val action = EditDeckFragmentDirections.actionEditSetFragmentToSetsFragment()
                findNavController().navigate(action)
            }
        }
    }
}