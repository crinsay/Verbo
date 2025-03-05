package com.example.verbo.views.fragments

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.verbo.R
import com.example.verbo.adapters.LanguagesRecyclerViewAdapter
import com.example.verbo.databinding.FragmentLanguagesBinding
import com.example.verbo.viewmodels.LanguagesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LanguagesFragment : Fragment() {
    private lateinit var binding: FragmentLanguagesBinding
    private lateinit var languagesAdapter: LanguagesRecyclerViewAdapter
    private val viewModel: LanguagesViewModel by viewModels()

    companion object {
        fun newInstance() = LanguagesFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getAllLanguages()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        languagesAdapter = LanguagesRecyclerViewAdapter(mutableListOf())
        languagesAdapter.onItemClickListener = { languageId ->
            val action = LanguagesFragmentDirections.actionLanguagesListFragmentToAddLanguageFragment(languageId)
            findNavController().navigate(action)
        }
        languagesAdapter.onItemLongClickListener = { view, language, position ->
            val popupMenu = PopupMenu(requireContext(), view)

            popupMenu.menuInflater
                .inflate(R.menu.menu_language_delete, popupMenu.menu)
            popupMenu.gravity = Gravity.END

            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.edit_option -> {
                        val action = LanguagesFragmentDirections.actionLanguagesListFragmentToAddLanguageFragment(language.languageId) //0L by default.
                        findNavController().navigate(action)
                        true
                    }
                    R.id.delete_option -> {
                        lifecycleScope.launch {
                            viewModel.deleteLanguage(language)
                            languagesAdapter.itemRemoved(position)
                        }
                        true
                    }
                    else -> false
                }
            }

            popupMenu.show()
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.languages.collect {
                    languagesAdapter.fillWithData(it.toMutableList())
                }
            }
        }

        binding = FragmentLanguagesBinding.inflate(inflater, container, false)
        binding.apply {
            recyclerViewLanguages.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = languagesAdapter
            }
        }

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            backButton.setOnClickListener {
                val action = LanguagesFragmentDirections.actionLanguagesListFragmentToMenuFragment()
                findNavController().navigate(action)
            }

            addLanguageButton.setOnClickListener {
                val action = LanguagesFragmentDirections.actionLanguagesListFragmentToAddLanguageFragment(0L) //0L by default.
                findNavController().navigate(action)
            }
        }
    }
}