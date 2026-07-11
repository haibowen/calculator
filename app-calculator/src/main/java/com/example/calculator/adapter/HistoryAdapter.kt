package com.example.calculator.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.calculator.databinding.ItemHistoryBinding
import com.example.calculator.model.HistoryEntry

class HistoryAdapter(private val onClick: (HistoryEntry) -> Unit) :
    ListAdapter<HistoryEntry, HistoryAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemHistoryBinding,
        private val onClick: (HistoryEntry) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(entry: HistoryEntry) {
            binding.tvHistoryExpr.text = entry.expression
            binding.tvHistoryResult.text = "= ${entry.result}"
            binding.root.setOnClickListener { onClick(entry) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<HistoryEntry>() {
        override fun areItemsTheSame(a: HistoryEntry, b: HistoryEntry) = a.id == b.id
        override fun areContentsTheSame(a: HistoryEntry, b: HistoryEntry) = a == b
    }
}
