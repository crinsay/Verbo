package com.example.verbo.languageslist

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupMenu
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.verbo.R
import com.example.verbo.adapters.LanguagesRecyclerViewAdapter
import com.example.verbo.databinding.FragmentLanguagesListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LanguagesListFragment : Fragment() {

    private lateinit var binding: FragmentLanguagesListBinding
    private lateinit var languagesAdapter: LanguagesRecyclerViewAdapter

    companion object {
        fun newInstance() = LanguagesListFragment()
    }

    private val viewModel: LanguagesListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.loadAllLanguages()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        languagesAdapter = LanguagesRecyclerViewAdapter(mutableListOf())

        //For test:
        languagesAdapter.onItemClickListener = { languageId ->
            val action = LanguagesListFragmentDirections.actionLanguagesListFragmentToAddLanguageFragment(languageId)
            findNavController().navigate(action)
        }

        languagesAdapter.onItemLongClickListener = { view, language, position ->
            val popupMenu = PopupMenu(requireContext(), view)

            popupMenu.menuInflater
                .inflate(R.menu.menu_language_delete, popupMenu.menu)
            popupMenu.gravity = Gravity.END


            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
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

            // Wyświetlamy menu
            popupMenu.show()
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.languages.collect {
                    languagesAdapter.fillWithData(it.toMutableList())
                }
            }
        }

        binding = FragmentLanguagesListBinding.inflate(inflater, container, false)
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
                val action = LanguagesListFragmentDirections.actionLanguagesListFragmentToMenuFragment()
                findNavController().navigate(action)
            }
            addLanguageButton.setOnClickListener {
                val action = LanguagesListFragmentDirections.actionLanguagesListFragmentToAddLanguageFragment() //0L by default.
                findNavController().navigate(action)
            }
        }
    }
}