package com.example.verbo.adapters.recyclerview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.verbo.common.dtos.FlashcardDto
import com.example.verbo.databinding.RecyclerViewElementFlashcardBinding

class FlashcardsRecyclerViewAdapter(private var items: MutableList<FlashcardDto>) : RecyclerView.Adapter<FlashcardsRecyclerViewAdapter.ViewHolder>() {

    var onItemLongClickListener: ((View, FlashcardDto, Int) -> Unit)? = null

    companion object {
        fun create(): FlashcardsRecyclerViewAdapter {
            val adapter = FlashcardsRecyclerViewAdapter(mutableListOf())
            return adapter
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecyclerViewElementFlashcardBinding.inflate(inflater, parent, false)

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

    @SuppressLint("NotifyDataSetChanged")
    fun fillWithData(newItems: MutableList<FlashcardDto>) {
        items = newItems
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: RecyclerViewElementFlashcardBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FlashcardDto) {
            binding.apply {
                textViewWord.text = item.wordDefinition
                textViewTranslate.text = item.wordTranslation
            }

            itemView.setOnLongClickListener {
                onItemLongClickListener?.invoke(it, item, adapterPosition)
                true
            }
        }
    }
}