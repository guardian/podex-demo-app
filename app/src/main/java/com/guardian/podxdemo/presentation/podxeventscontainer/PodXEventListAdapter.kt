package com.guardian.podxdemo.presentation.podxeventscontainer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.guardian.core.podxevent.PodXEvent
import com.guardian.podxdemo.R
import com.guardian.podxdemo.databinding.ViewholderPodxeventadapterImageBinding
import com.guardian.podxdemo.presentation.common.DataBoundListAdapter
import java.util.concurrent.Executor

/**
 * A custom list adapter that handles the different PodXEvent types and
 */
class PodXEventListAdapter(
    callback: DiffUtil.ItemCallback<PodXEvent>,
    executor: Executor,
    val handleSelection: (PodXEvent) -> Unit
)  : DataBoundListAdapter<PodXEvent, ViewholderPodxeventadapterImageBinding>(callback, executor) {
    override fun createBinding(parent: ViewGroup): ViewholderPodxeventadapterImageBinding {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.viewholder_podxeventadapter_image,
            parent,
            false
        )
    }

    override fun bind(holder: ViewholderPodxeventadapterImageBinding, item: PodXEvent) {
        holder.podXEvent = item
    }
}