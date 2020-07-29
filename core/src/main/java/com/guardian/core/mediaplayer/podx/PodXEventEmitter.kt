package com.guardian.core.mediaplayer.podx

import androidx.lifecycle.LiveData
import com.guardian.core.feeditem.FeedItem
import com.guardian.core.podxevent.PodXCallPromptEvent
import com.guardian.core.podxevent.PodXFeedBackEvent
import com.guardian.core.podxevent.PodXFeedLinkEvent
import com.guardian.core.podxevent.PodXImageEvent
import com.guardian.core.podxevent.PodXNewsLetterSignUpEvent
import com.guardian.core.podxevent.PodXPollEvent
import com.guardian.core.podxevent.PodXSocialPromptEvent
import com.guardian.core.podxevent.PodXSupportEvent
import com.guardian.core.podxevent.PodXTextEvent
import com.guardian.core.podxevent.PodXWebEvent

/**
 * A [LiveData] that emits the list of currently active [PodXImageEvent]s synchronised with the current
 * [MediaService]. the [PodXImageEvent]s are set based on the [FeedItem] that has been passed in, so
 * at this stage it is possible for the FeedItem to be out of sync with the one which is currently
 * being played back.
 */
interface PodXEventEmitter {
    /**
     * A LiveData which contains a list of currently showing [PodXImageEvent]s
     */
    val podXImageEventLiveData: LiveData<List<PodXImageEvent>>

    /**
     * A LiveData which contains a list of [PodXImageEvent]s that will be shown in a currently
     * playing episode
     */
    val podXPendingImageEventLiveData: LiveData<List<PodXImageEvent>>

    /**
     * A LiveData which contains a list of currently showing [PodXWebEvent]s
     */
    val podXWebEventLiveData: LiveData<List<PodXWebEvent>>

    /**
     * A LiveData which contains a list of [PodXWebEvent]s that will be shown in a currently
     * playing episode
     */
    val podXPendingWebEventLiveData: LiveData<List<PodXWebEvent>>

    /**
     * A LiveData which contains a list of currently showing [PodXSupportEvent]s
     */
    val podXSupportEventLiveData: LiveData<List<PodXSupportEvent>>

    /**
     * A LiveData which contains a list of [PodXSupportEvent]s that will be shown in a currently
     * playing episode
     */
    val podXPendingSupportEventLiveData: LiveData<List<PodXSupportEvent>>

    /**
     * A LiveData which contains a list of currently showing [PodXCallPromptEvent]s
     */
    val podXCallPromptEventLiveData: LiveData<List<PodXCallPromptEvent>>

    /**
     * A LiveData which contains a list of [PodXCallPromptEvent]s that will be shown in a currently
     * playing episode
     */
    val podXPendingCallPromptEventLiveData: LiveData<List<PodXCallPromptEvent>>

    /**
     * A LiveData which contains a list of currently showing [PodXFeedBackEvent]s
     */
    val podXFeedBackEventLiveData: LiveData<List<PodXFeedBackEvent>>

    /**
     * A LiveData which contains a list of [PodXFeedBackEvent]s that will be shown in a currently
     * playing episode
     */
    val podXPendingFeedBackEventLiveData: LiveData<List<PodXFeedBackEvent>>

    /**
     * A LiveData which contains a list of currently showing [PodXFeedLinkEvent]s
     */
    val podXFeedLinkEventLiveData: LiveData<List<PodXFeedLinkEvent>>

    /**
     * A LiveData which contains a list of [PodXFeedLinkEvent]s that will be shown in a currently
     * playing episode
     */
    val podXPendingFeedLinkEventLiveData: LiveData<List<PodXFeedLinkEvent>>

    /**
     * A LiveData which contains a list of currently showing [PodXNewsLetterSignUpEvent]s
     */
    val podXNewsLetterSignUpEventLiveData: LiveData<List<PodXNewsLetterSignUpEvent>>

    /**
     * A LiveData which contains a list of [PodXNewsLetterSignUpEvent]s that will be shown in a currently
     * playing episode
     */
    val podXPendingNewsLetterSignUpEventLiveData: LiveData<List<PodXNewsLetterSignUpEvent>>

    /**
     * A LiveData which contains a list of currently showing [PodXPollEvent]s
     */
    val podXPollEventLiveData: LiveData<List<PodXPollEvent>>

    /**
     * A LiveData which contains a list of [PodXPollEvent]s that will be shown in a currently
     * playing episode
     */
    val podXPendingPollEventLiveData: LiveData<List<PodXPollEvent>>

    /**
     * A LiveData which contains a list of currently showing [PodXSocialPromptEvent]s
     */
    val podXSocialPromptEventLiveData: LiveData<List<PodXSocialPromptEvent>>

    /**
     * A LiveData which contains a list of [PodXSocialPromptEvent]s that will be shown in a currently
     * playing episode
     */
    val podXPendingSocialPromptEventLiveData: LiveData<List<PodXSocialPromptEvent>>

    /**
     * A LiveData which contains a list of currently showing [PodXTextEvent]s
     */
    val podXTextEventLiveData: LiveData<List<PodXTextEvent>>

    /**
     * A LiveData which contains a list of [PodXTextEvent]s that will be shown in a currently
     * playing episode
     */
    val podXPendingTextEventLiveData: LiveData<List<PodXTextEvent>>



    /**
     * Register a [FeedItem] with the emitter. Events associated with the feed item will fire
     * according to the playback time taken from the currently instantiated [MediaService].
     *
     * @param feedItem this should be the currently playing feed item
     */
    fun registerCurrentFeedItem(feedItem: FeedItem)
}
