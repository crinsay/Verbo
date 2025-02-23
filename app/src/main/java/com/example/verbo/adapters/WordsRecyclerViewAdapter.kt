package com.example.verbo.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.verbo.common.dtos.FlashcardDto
import com.example.verbo.databinding.FragmentSetBinding
import com.example.verbo.databinding.FragmentWordBinding

class WordsRecyclerViewAdapter(private var items: MutableList<FlashcardDto>) : RecyclerView.Adapter<WordsRecyclerViewAdapter.ViewHolder>() {

    var onItemClickListener: ((Long) -> Unit)? = null
    var onItemLongClickListener: ((View, FlashcardDto, Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FragmentWordBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun itemRemoved(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    fun fillWithData(newItems: MutableList<FlashcardDto>) {
        items = newItems
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: FragmentWordBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FlashcardDto) {
            binding.apply {
                textViewWord.text = item.wordDefinition
                textViewTranslate.text = item.wordTranslation
            }

            itemView.setOnClickListener {
                onItemClickListener?.invoke(item.flashcardId)
            }

            itemView.setOnLongClickListener {
                onItemLongClickListener?.invoke(it, item, adapterPosition)
                true
            }
        }
    }
}