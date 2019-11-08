package com.guardian.core.mediaplayer.podx

import androidx.lifecycle.LiveData
import com.guardian.core.feeditem.FeedItem
import com.guardian.core.podxevent.PodXEvent

/**
 * A [LiveData] that emits the list of currently active [PodXEvent]s synchronised with the current
 * [MediaService]. the [PodXEvent]s are set based on the [FeedItem] that has been passed in, so
 * at this stage it is possible for the FeedItem to be out of sync with the one which is currently
 * being played back.
 */
interface PodXEventEmitter {
    /**
     * A LiveData which contains a list of currently showing PodXEvents
     */
    val podXEventLiveData: LiveData<List<PodXEvent>>

    /**
     * Register a [FeedItem] with the emitter. Events associated with the feed item will fire
     * according to the playback time taken from the currently instantiated [MediaService].
     *
     * @param feedItem this should be the currently playing feed item
     */
    fun registerCurrentFeedItem(feedItem: FeedItem)
}