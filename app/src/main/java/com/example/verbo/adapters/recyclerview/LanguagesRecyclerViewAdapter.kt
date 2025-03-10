package com.example.verbo.adapters.recyclerview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.verbo.common.dtos.LanguageDto
import com.example.verbo.databinding.RecyclerViewElementLanguageBinding

class LanguagesRecyclerViewAdapter(private var items: MutableList<LanguageDto>)
    : RecyclerView.Adapter<LanguagesRecyclerViewAdapter.ViewHolder>(){

    var onItemClickListener: ((Long) -> Unit)? = null
    var onItemLongClickListener: ((View, LanguageDto, Int) -> Unit)? = null

    companion object {
        fun create(): LanguagesRecyclerViewAdapter {
            val adapter = LanguagesRecyclerViewAdapter(mutableListOf())
            return adapter
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecyclerViewElementLanguageBinding.inflate(inflater, parent, false)

        return ViewHolder(binding)
    }

    fun itemRemoved(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(private val binding: RecyclerViewElementLanguageBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LanguageDto) {
            binding.apply {
                textViewLanguageName.text = item.name
            }

            itemView.setOnClickListener {
                onItemClickListener?.invoke(item.languageId)
            }

            itemView.setOnLongClickListener {
                onItemLongClickListener?.invoke(it, item, adapterPosition)
                true
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun fillWithData(newItems: MutableList<LanguageDto>) {
        items = newItems
        notifyDataSetChanged()
    }
}