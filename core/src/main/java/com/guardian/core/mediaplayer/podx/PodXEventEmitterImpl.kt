package com.guardian.core.mediaplayer.podx

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.guardian.core.feeditem.FeedItem
import com.guardian.core.mediaplayer.common.MediaSessionConnection
import com.guardian.core.podxevent.PodXEvent
import com.guardian.core.podxevent.dao.PodXEventDao
import java.util.PriorityQueue
import javax.inject.Inject

class PodXEventEmitterImpl
@Inject constructor(private val mediaSessionConnection: MediaSessionConnection,
                    private val podXEventDao: PodXEventDao) :
    PodXEventEmitter {
    private val podXEventMutableLiveData = MutableLiveData<PodXEvent>()
    override val podXEventLiveData: LiveData<PodXEvent> = podXEventMutableLiveData

    private val podXEventQueue: PriorityQueue<PodXEvent> = PriorityQueue(30)
        {o1, o2 ->
            (o1.timeStart - o2.timeStart).toInt()
        }

    override fun registerCurrentFeedItem(feedItem: FeedItem) {
        //podXEventDao.getPodXEventsForFeedItemUrl(feedItem.feedItemAudioUrl)
    }
}