package com.guardian.podxdemo.presentation.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.guardian.core.feed.FeedItem
import com.guardian.podxdemo.R
import com.guardian.podxdemo.databinding.ViewholderFeedadapterFeeditemBinding
import com.guardian.podxdemo.presentation.common.DataBoundListAdapter
import java.util.concurrent.Executor

class FeedListAdapter(
    callback: DiffUtil.ItemCallback<FeedItem>,
    executor: Executor,
    val handleSelection: (FeedItem) -> Unit
) :
    DataBoundListAdapter<FeedItem, ViewholderFeedadapterFeeditemBinding>(callback, executor) {
    override fun createBinding(parent: ViewGroup): ViewholderFeedadapterFeeditemBinding {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.viewholder_feedadapter_feeditem,
            parent,
            false
        )
    }

    override fun bind(holder: ViewholderFeedadapterFeeditemBinding, item: FeedItem) {
        holder.feedItem = item
        holder.root.setOnClickListener { handleSelection(item) }
    }
}