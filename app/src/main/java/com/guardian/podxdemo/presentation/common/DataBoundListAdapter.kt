package com.guardian.podxdemo.presentation.common

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 * A generic ListAdapter with the boilerplate involved in setting a recyclerview viewholder's data
 * using the data binding library.
 *
 * @param T : A data class to bind to.
 * @param V : [ViewDataBinding] for the viewholder
 */

// todo ensure the asyc differ is actually running asyc
abstract class DataBoundListAdapter<T, V : ViewDataBinding> (
    callback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, DataBoundListAdapter.DataBoundViewHolder<V>>(
    AsyncDifferConfig.Builder<T>(callback)
        .build()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBoundViewHolder<V> {
        return DataBoundViewHolder(
            createBinding(parent)
        )
    }

    abstract fun createBinding(parent: ViewGroup): V

    override fun onBindViewHolder(holder: DataBoundViewHolder<V>, position: Int) {
        bind(holder.binding, getItem(position))
    }

    abstract fun bind(holder: V, item: T)

    class DataBoundViewHolder<out T : ViewDataBinding> constructor(val binding: T) :
        RecyclerView.ViewHolder(binding.root)
}