package com.example.verbo.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.verbo.common.dtos.DeckDto
import com.example.verbo.common.dtos.LanguageDto
import com.example.verbo.databinding.FragmentSetBinding



class SetRecyclerViewAdapter (private var items: MutableList<DeckDto>) : RecyclerView.Adapter<SetRecyclerViewAdapter.ViewHolder>()
{
    var onItemClickListener: ((Long) -> Unit)? = null
    var onItemLongClickListener: ((View, DeckDto, Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = FragmentSetBinding.inflate(inflater, parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(private val binding: FragmentSetBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DeckDto){
            binding.apply {
                textViewSetName.text = item.name
            }

            itemView.setOnClickListener {
                onItemClickListener?.invoke(item.deckId)
            }

            itemView.setOnLongClickListener {
                onItemLongClickListener?.invoke(it, item, adapterPosition)
                true
            }
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun fillWithData(newItems: MutableList<DeckDto>) {
        items = newItems
        notifyDataSetChanged()
    }

}