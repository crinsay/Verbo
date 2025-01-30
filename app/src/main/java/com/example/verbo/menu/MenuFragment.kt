package com.example.verbo.menu

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.verbo.R

class MenuFragment : Fragment() {

    companion object {
        fun newInstance() = MenuFragment()
    }

    private val viewModel: MenuViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val zestawy = view.findViewById<Button>(R.id.Zestawy)
        val jezyki = view.findViewById<Button>(R.id.Jezyki)

        jezyki.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_languagesListFragment)
        }
        zestawy.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_setsFragment)
        }


    }
}