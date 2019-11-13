package com.guardian.podxdemo.presentation.podxeventscontainer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.guardian.core.podxevent.PodXImageEvent
import com.guardian.podxdemo.R
import com.guardian.podxdemo.databinding.ViewholderPodxeventadapterImageBinding
import com.guardian.podxdemo.presentation.common.DataBoundListAdapter
import java.util.concurrent.Executor

class PodXEventListAdapter(
    callback: DiffUtil.ItemCallback<PodXImageEvent>,
    executor: Executor,
    val handleSelection: (PodXImageEvent) -> Unit
)  : DataBoundListAdapter<PodXImageEvent, ViewholderPodxeventadapterImageBinding>(callback, executor) {
    override fun createBinding(parent: ViewGroup): ViewholderPodxeventadapterImageBinding {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.viewholder_podxeventadapter_image,
            parent,
            false
        )
    }

    override fun bind(holder: ViewholderPodxeventadapterImageBinding, item: PodXImageEvent) {
        holder.podXImageEvent = item
        holder.root.setOnClickListener { handleSelection(item) }
    }
}