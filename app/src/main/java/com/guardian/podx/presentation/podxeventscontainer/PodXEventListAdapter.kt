package com.guardian.podx.presentation.podxeventscontainer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.guardian.podx.R
import com.guardian.podx.databinding.ViewholderPodxeventadapterImageBinding
import com.guardian.podx.presentation.common.DataBoundListAdapter
import java.util.concurrent.Executor

class PodXEventListAdapter(
    callback: DiffUtil.ItemCallback<PodXEventThumbnailData>,
    executor: Executor,
    val navigateToTimestampMethod: (Long) -> Unit
) : DataBoundListAdapter<PodXEventThumbnailData, ViewholderPodxeventadapterImageBinding>(callback, executor) {
    override fun createBinding(parent: ViewGroup): ViewholderPodxeventadapterImageBinding {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.viewholder_podxeventadapter_image,
            parent,
            false
        )
    }

    override fun bind(holder: ViewholderPodxeventadapterImageBinding, item: PodXEventThumbnailData) {
        holder.podXThumbnailData = item
        holder.imageviewPodxeventviewholderBadge.setImageDrawable(item.badgeDrawable)
        if (item.imageDrawable == null && item.imageUrlString.isNullOrBlank()) {
            holder.imageviewPodxeventviewholderBadge.visibility = View.INVISIBLE
            holder.imageviewPodxeventviewholderBadgeBackground.visibility = View.INVISIBLE
        }
        // initialise collapsed
        holder.textviewPodxeventviewholderContractedNotification.visibility = View.VISIBLE
        holder.textviewPodxeventviewholderExpandedNotification.visibility = View.GONE
        holder.textviewPodxeventviewholderExpandedCaption.visibility = View.GONE
        holder.buttonPodxeventviewholderExpand.rotationX = 0f

        holder.buttonPodxeventviewholderExpand.setOnClickListener {
            expandButtonVisibilitySwitch(holder, item)
            rotateExpandButtonChevron(holder)
        }

        item.expandSwitch.observeForever {
            holder.textviewPodxeventviewholderContractedNotification
                .visibility = if (it) {
                View.VISIBLE
            } else {
                View.GONE
            }
            expandButtonVisibilitySwitch(holder, item)
            rotateExpandButtonChevron(holder)
        }

        holder.buttonPodxeventviewholderSkipToTimestamp.setOnClickListener {
            navigateToTimestampMethod(item.timeStart)
        }
    }

    private fun expandButtonVisibilitySwitch
    (holder: ViewholderPodxeventadapterImageBinding, item: PodXEventThumbnailData) {
        // set up image expand binding
        if (item.imageSwitch) {
            if (holder.textviewPodxeventviewholderContractedNotification.visibility == View.VISIBLE) {
                holder.imageviewPodxeventviewholderExpanded.visibility = View.VISIBLE
                holder.imageviewPodxeventviewholderContracted.visibility = View.GONE
            } else {
                holder.imageviewPodxeventviewholderExpanded.visibility = View.GONE
                holder.imageviewPodxeventviewholderContracted.visibility = View.VISIBLE
            }
        }

        if (holder.textviewPodxeventviewholderContractedNotification.visibility == View.VISIBLE) {
            holder.textviewPodxeventviewholderContractedNotification.visibility = View.INVISIBLE
            holder.textviewPodxeventviewholderExpandedNotification.visibility = View.VISIBLE
            holder.textviewPodxeventviewholderExpandedCaption.visibility = View.VISIBLE
            holder.buttonPodxeventviewholderSkipToTimestamp.visibility = View.VISIBLE
            holder.textviewPodxeventviewholderTimestamp.visibility = View.VISIBLE
        } else {
            holder.textviewPodxeventviewholderContractedNotification.visibility = View.VISIBLE
            holder.textviewPodxeventviewholderExpandedNotification.visibility = View.GONE
            holder.textviewPodxeventviewholderExpandedCaption.visibility = View.GONE
            holder.buttonPodxeventviewholderSkipToTimestamp.visibility = View.GONE
            holder.textviewPodxeventviewholderTimestamp.visibility = View.GONE
        }
    }

    private fun rotateExpandButtonChevron(holder: ViewholderPodxeventadapterImageBinding) {
        // rotate the expand chevron, nb. rotation of the actual view is applied before the
        // animation starts
        holder.buttonPodxeventviewholderExpand.startAnimation(
            RotateAnimation(
                180f, 0f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f
            ).apply {
                duration = 500
                interpolator = AccelerateDecelerateInterpolator()
            }
        )

        if (holder.textviewPodxeventviewholderContractedNotification.visibility == View.VISIBLE) {
            holder.buttonPodxeventviewholderExpand.rotationX = 0f
        } else {
            holder.buttonPodxeventviewholderExpand.rotationX = 180f
        }
    }
}
