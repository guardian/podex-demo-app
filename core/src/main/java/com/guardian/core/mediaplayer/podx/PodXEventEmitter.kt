package com.guardian.core.mediaplayer.podx

import androidx.lifecycle.LiveData
import com.guardian.core.feeditem.FeedItem
import com.guardian.core.podxevent.PodXEvent

interface PodXEventEmitter {
    val podXEventLiveData: LiveData<PodXEvent?>
    fun registerCurrentFeedItem(feedItem: FeedItem)
}