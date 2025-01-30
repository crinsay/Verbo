package com.example.verbo.languageslist

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.verbo.R

class LanguagesListFragment : Fragment() {

    companion object {
        fun newInstance() = LanguagesListFragment()
    }

    private val viewModel: LanguagesListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_languages_list, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val powrot = view.findViewById<Button>(R.id.Powrot)
        val dodaj = view.findViewById<Button>(R.id.DodajJezyk)

        powrot.setOnClickListener {
            findNavController().navigate(R.id.action_languagesListFragment_to_menuFragment)
        }
        dodaj.setOnClickListener {
            findNavController().navigate(R.id.action_languagesListFragment_to_addLanguageFragment)
        }

    }
}