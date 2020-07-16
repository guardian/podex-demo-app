package com.guardian.podx.presentation.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.guardian.core.search.SearchResult
import com.guardian.podx.R
import com.guardian.podx.databinding.ViewholderSearchadapterResultBinding
import com.guardian.podx.presentation.common.DataBoundListAdapter
import java.util.concurrent.Executor

class SearchListAdapter(
    callback: DiffUtil.ItemCallback<SearchResult>,
    executor: Executor,
    val handleSelection: (SearchResult) -> Unit
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
        holder.root.setOnClickListener { handleSelection(item) }
    }
}