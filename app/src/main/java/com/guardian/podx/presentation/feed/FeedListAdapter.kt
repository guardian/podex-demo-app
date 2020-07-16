package com.guardian.podx.presentation.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import com.guardian.core.feeditem.FeedItem
import com.guardian.podx.R
import com.guardian.podx.databinding.ViewholderFeedadapterFeeditemBinding
import com.guardian.podx.presentation.common.DataBoundListAdapter
import java.util.concurrent.Executor

class FeedListAdapter(
    callback: DiffUtil.ItemCallback<FeedItem>,
    executor: Executor,
    val handleSelection: (FeedItem) -> Unit,
    val handlePlayPause: (FeedItem) -> Unit,
    val bindIsPlaying :(FeedItem) -> LiveData<Boolean>
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
        holder.imagebuttonFeeditemPlaypause.setOnClickListener { handlePlayPause(item) }
        bindIsPlaying(item).observeForever { isPlaying ->
            holder.imagebuttonFeeditemPlaypause.setImageResource(
                if (isPlaying) {
                    R.drawable.baseline_pause_circle_filled_black_48
                } else {
                    R.drawable.baseline_play_circle_filled_black_48
                }
            )
        }
    }
}