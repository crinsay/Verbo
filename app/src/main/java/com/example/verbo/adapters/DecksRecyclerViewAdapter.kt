package com.example.verbo.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.verbo.common.dtos.DeckDto
import com.example.verbo.databinding.RecyclerViewElementDeckBinding


class DecksRecyclerViewAdapter (private var items: MutableList<DeckDto>) : RecyclerView.Adapter<DecksRecyclerViewAdapter.ViewHolder>()
{
    var onItemClickListener: ((Long) -> Unit)? = null
    var onItemLongClickListener: ((View, DeckDto, Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = RecyclerViewElementDeckBinding.inflate(inflater, parent, false)

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

    inner class ViewHolder(private val binding: RecyclerViewElementDeckBinding): RecyclerView.ViewHolder(binding.root) {
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