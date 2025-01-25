package com.example.verbo.AddSet

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.verbo.R

class AddSetFragment : Fragment() {

    companion object {
        fun newInstance() = AddSetFragment()
    }

    private val viewModel: AddSetViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_add_set, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val anuluj = view.findViewById<Button>(R.id.Anuluj)

        anuluj.setOnClickListener {
            findNavController().navigate(R.id.action_addSetFragment_to_setsFragment)
        }

    }
}