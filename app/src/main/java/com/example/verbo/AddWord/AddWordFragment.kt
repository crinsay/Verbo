package com.example.verbo.AddWord

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.verbo.R

class AddWordFragment : Fragment() {

    companion object {
        fun newInstance() = AddWordFragment()
    }

    private val viewModel: AddWordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_add_word, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val zakoncz = view.findViewById<Button>(R.id.Zakoncz)
        val dalej = view.findViewById<Button>(R.id.Dalej)


        zakoncz.setOnClickListener {
            findNavController().navigate(R.id.action_addWordFragment_to_languageSetsFragment)
        }

    }
}