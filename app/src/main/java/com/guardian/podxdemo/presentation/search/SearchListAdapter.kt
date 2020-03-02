package com.guardian.podxdemo.presentation.search

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.guardian.core.search.SearchResult
import com.guardian.podxdemo.R
import com.guardian.podxdemo.databinding.ViewholderSearchadapterResultBinding
import com.guardian.podxdemo.presentation.common.DataBoundListAdapter
import java.util.concurrent.Executor

class SearchListAdapter(
    callback: DiffUtil.ItemCallback<SearchResult>,
    executor: Executor,
    val handleSelection: (SearchResult, ImageView) -> Unit
) :
    DataBoundListAdapter<SearchResult, ViewholderSearchadapterResultBinding>(callback, executor) {
    override fun createBinding(parent: ViewGroup): ViewholderSearchadapterResultBinding {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.viewholder_searchadapter_result,
            parent,
            false
        )
    }

    override fun bind(holder: ViewholderSearchadapterResultBinding, item: SearchResult) {
        holder.searchResult = item
        holder.root.setOnClickListener { handleSelection(item, holder.imageviewSearch) }
    }
}