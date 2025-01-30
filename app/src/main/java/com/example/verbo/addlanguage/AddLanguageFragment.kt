package com.example.verbo.addlanguage

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.verbo.R

class AddLanguageFragment : Fragment() {

    companion object {
        fun newInstance() = AddLanguageFragment()
    }

    private val viewModel: AddLanguageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_add_language, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val anuluj = view.findViewById<Button>(R.id.Anuluj)

        anuluj.setOnClickListener {
            findNavController().navigate(R.id.action_addLanguageFragment_to_languagesListFragment)
        }

    }
}