package com.example.verbo.addset

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.verbo.R
import com.example.verbo.databinding.FragmentAddLanguageBinding
import com.example.verbo.databinding.FragmentAddSetBinding
import com.example.verbo.databinding.FragmentSetsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddSetFragment : Fragment() {

    private val args : AddSetFragmentArgs by navArgs()
    private  lateinit var binding : FragmentAddSetBinding
    private val viewModel: AddSetViewModel by viewModels()

    companion object {
        fun newInstance() = AddSetFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (args.setId != 0L){
            viewModel.getDeck(args.setId)
        }
        viewModel.setLanguageId(args.languageId)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddSetBinding.inflate(inflater, container, false)
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

                        viewModel.saveDeck(args.setId)
                        val action =
                            AddSetFragmentDirections.actionAddSetFragmentToAddWordFragment(args.setId)
                        findNavController().navigate(action)
                    }

            }
            cancelSetButton.setOnClickListener {
                val action = AddSetFragmentDirections.actionAddSetFragmentToSetsFragment()
                findNavController().navigate(action)
            }

        }

    }
}