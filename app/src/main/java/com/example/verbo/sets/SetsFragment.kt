package com.example.verbo.sets

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.verbo.R

class SetsFragment : Fragment() {

    companion object {
        fun newInstance() = SetsFragment()
    }

    private val viewModel: SetsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_sets, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val powrot = view.findViewById<Button>(R.id.Powrot)
        val dodaj = view.findViewById<Button>(R.id.buttonAddSet)


        powrot.setOnClickListener {
            findNavController().navigate(R.id.action_setsFragment_to_menuFragment)
        }
        dodaj.setOnClickListener {
            findNavController().navigate(R.id.action_setsFragment_to_addSetFragment)
        }


    }
}