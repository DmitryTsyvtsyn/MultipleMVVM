package io.github.dmitrytsyvtsyn.multiple_mvvm.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.dmitrytsyvtsyn.multiple_mvvm.viewmodel.ColorsViewModel
import io.github.dmitrytsyvtsyn.multiple_mvvm.viewmodel.MainItemViewModel
import io.github.dmitrytsyvtsyn.multiple_mvvm.viewmodel.NumbersViewModel

class MainAdapter(private val items: List<MainItemViewModel>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            NUMBERS -> NumbersViewHolder(parent.context)
            COLORS -> ColorsViewHolder(parent.context)
            else -> error("Illegal viewType -> $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NumbersViewHolder -> holder.bind(items[position] as NumbersViewModel)
            is ColorsViewHolder -> holder.bind(items[position] as ColorsViewModel)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(items[position]) {
            is NumbersViewModel -> NUMBERS
            is ColorsViewModel -> COLORS
        }
    }

    companion object {
        private const val NUMBERS = 1
        private const val COLORS = 2
    }
}