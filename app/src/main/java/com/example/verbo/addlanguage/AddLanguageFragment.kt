package com.example.verbo.addlanguage

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.verbo.R
import com.example.verbo.databinding.FragmentAddLanguageBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddLanguageFragment : Fragment() {
    private val args: AddLanguageFragmentArgs by navArgs()
    private lateinit var binding: FragmentAddLanguageBinding

    companion object {
        fun newInstance() = AddLanguageFragment()
    }

    private val viewModel: AddLanguageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (args.languageId != 0L) {
            viewModel.getLanguage(args.languageId)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddLanguageBinding.inflate(inflater, container, false)
        binding.languageViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            saveLanguageButton.setOnClickListener {
                lifecycleScope.launch {
                    viewModel.saveLanguage(args.languageId)
                    val action = AddLanguageFragmentDirections.actionAddLanguageFragmentToLanguagesListFragment()
                    findNavController().navigate(action)
                }
            }

           cancelButton.setOnClickListener {
               val action = AddLanguageFragmentDirections.actionAddLanguageFragmentToLanguagesListFragment()
               findNavController().navigate(action)
           }
        }

    }
}