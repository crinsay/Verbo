package com.example.verbo.editword

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.verbo.R
import com.example.verbo.databinding.FragmentEditSetBinding
import com.example.verbo.databinding.FragmentEditWordBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditWordFragment : Fragment() {

    private val args: EditWordFragmentArgs by navArgs()
    private lateinit var binding: FragmentEditWordBinding

    companion object {
        fun newInstance() = EditWordFragment()
    }

    private val viewModel: EditWordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setDeckId(args.deckId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditWordBinding.inflate(inflater, container, false)
        binding.editWordViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadFlashcard(args.flashcardId)

        binding.Zapisz.setOnClickListener {
            viewModel.updateFlashcard(args.flashcardId)
            val action = EditWordFragmentDirections.actionEditWordFragmentToEditSetFragment(args.deckId)
            findNavController().navigate(action)
        }
        binding.Anuluj.setOnClickListener{
            val action = EditWordFragmentDirections.actionEditWordFragmentToEditSetFragment(args.deckId)
            findNavController().navigate(action)
        }
    }
}