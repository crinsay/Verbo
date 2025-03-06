package com.example.verbo.views.fragments

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
import com.example.verbo.databinding.FragmentAddDeckBinding
import com.example.verbo.viewmodels.AddDeckViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddDeckFragment : Fragment() {

    private val args : AddDeckFragmentArgs by navArgs()
    private  lateinit var binding : FragmentAddDeckBinding
    private val viewModel: AddDeckViewModel by viewModels()

    companion object {
        fun newInstance() = AddDeckFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.setLanguageId(args.languageId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddDeckBinding.inflate(inflater, container, false)
        binding.setViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply{
            saveDeckButton.setOnClickListener{
                val deckNameValue = viewModel.deckName.value
                    if (deckNameValue.isNullOrEmpty()) {
                        Toast.makeText(
                            context,
                            "Proszę uzupełnić nazwę zestawu",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        lifecycleScope.launch { //BO NIE NADAZA
                            viewModel.saveDeck()
                            val action = AddDeckFragmentDirections.actionAddSetFragmentToAddWordFragment(viewModel.deckId, args.languageId)
                            findNavController().navigate(action)
                        }

                    }

            }
            cancelSetButton.setOnClickListener {
                val action = AddDeckFragmentDirections.actionAddSetFragmentToSetsFragment()
                findNavController().navigate(action)
            }

        }

    }
}