package com.guardian.podxdemo.presentation.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.guardian.core.search.SearchResult
import com.guardian.podxdemo.R
import com.guardian.podxdemo.databinding.ViewholderSearchadapterResultBinding
import com.guardian.podxdemo.presentation.common.DataBoundListAdapter

class SearchListAdapter(callback: DiffUtil.ItemCallback<SearchResult>) :
    DataBoundListAdapter<SearchResult, ViewholderSearchadapterResultBinding>(callback) {
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
    }
}