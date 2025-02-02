package com.example.verbo.editlanguage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.verbo.databinding.FragmentEditLanguageBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditLanguageFragment : Fragment() {
    private val args: EditLanguageFragmentArgs by navArgs()
    private lateinit var binding: FragmentEditLanguageBinding
    private val viewModel: EditLanguageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadLanguage(args.languageId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditLanguageBinding.inflate(inflater, container, false)
        binding.editLanguageViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            saveLanguageButton.setOnClickListener {
                lifecycleScope.launch {
                    viewModel.updateLanguage(args.languageId)
                    val action = EditLanguageFragmentDirections.actionEditLanguageFragmentToLanguagesListFragment()
                    findNavController().navigate(action)
                }
            }

            cancelButton.setOnClickListener {
                val action = EditLanguageFragmentDirections.actionEditLanguageFragmentToLanguagesListFragment()
                findNavController().navigate(action)
            }
        }
    }
}