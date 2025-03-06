package com.example.verbo.adapters.spinner

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.verbo.common.dtos.DeckDto
import com.example.verbo.common.dtos.LanguageDto

class LanguagesSpinnerAdapter(context: Context, resource: Int, objects: List<String>)
    : ArrayAdapter<String>(context, resource, objects) {

    var onItemClickListener: ((Long) -> Unit)? = null
    lateinit var onItemSelectedListener: AdapterView.OnItemSelectedListener

    companion object {
        fun create(context: Context, resource: Int, languages: List<LanguageDto>): LanguagesSpinnerAdapter {
            val adapter = LanguagesSpinnerAdapter(context, resource, languages.map { it.name })
            adapter.setOnSelectedItemListener(languages)

            return adapter
        }
    }

    private fun setOnSelectedItemListener(languages: List<LanguageDto>) {
        onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                onItemClickListener?.invoke(languages[position].languageId)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }
}