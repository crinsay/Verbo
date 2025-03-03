package com.example.verbo.views.fragments

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.verbo.databinding.FragmentLanguageBinding
import com.example.verbo.viewmodels.LanguageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LanguageFragment : Fragment() {
    private val args: LanguageFragmentArgs by navArgs()
    private lateinit var binding: FragmentLanguageBinding

    companion object {
        fun newInstance() = LanguageFragment()
    }

    private val viewModel: LanguageViewModel by viewModels()

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
        binding = FragmentLanguageBinding.inflate(inflater, container, false)
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
                    val action = LanguageFragmentDirections.actionAddLanguageFragmentToLanguagesListFragment()
                    findNavController().navigate(action)
                }
            }

           cancelButton.setOnClickListener {
               val action = LanguageFragmentDirections.actionAddLanguageFragmentToLanguagesListFragment()
               findNavController().navigate(action)
           }
        }

    }
}