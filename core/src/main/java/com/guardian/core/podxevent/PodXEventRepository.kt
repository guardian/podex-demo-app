package com.guardian.core.podxevent

import com.guardian.core.feeditem.FeedItem
import io.reactivex.Flowable

/**
 * A Repository for accessing PodXEvents
 *
 * Currently all PodX Events are taken from the feed rss, and are associated with a given feed item.
 */
interface PodXEventRepository {
    /**
     * Returns a [Flowable] that emits a list of all [PodXImageEvent]s that are to be shown during the
     * playback of a [FeedItem] ordered by [PodXImageEvent.timeStart]
     *
     * @param feedItem feed item with to be associated with [PodXImageEvent] by it's
     * [FeedItem.feedUrlString]
     * @return a [Flowable] which emits the list of [PodXImageEvent]s and any updates to that list
     */
    fun getImageEventsForFeedItem(feedItem: FeedItem): Flowable<List<PodXImageEvent>>

    /**
     * Returns a [Flowable] that emits a list of all [PodXWebEvent]s that are to be shown during the
     * playback of a [FeedItem] ordered by [PodXWebEvent.timeStart]
     *
     * @param feedItem feed item with to be associated with [PodXWebEvent] by it's
     * [FeedItem.feedUrlString]
     * @return a [Flowable] which emits the list of [PodXWebEvent]s and any updates to that list
     */
    fun getWebEventsForFeedItem(feedItem: FeedItem): Flowable<List<PodXWebEvent>>

    /**
     * Returns a [Flowable] that emits a list of all [PodXSupportEvent]s that are to be shown during the
     * playback of a [FeedItem] ordered by [PodXSupportEvent.timeStart]
     *
     * @param feedItem feed item with to be associated with [PodXSupportEvent] by it's
     * [FeedItem.feedUrlString]
     * @return a [Flowable] which emits the list of [PodXSupportEvent]s and any updates to that list
     */
    fun getSupportEventsForFeedItem(feedItem: FeedItem): Flowable<List<PodXSupportEvent>>

    /**
     * Returns a [Flowable] that emits a list of all [PodXCallPromptEvent]s that are to be shown during the
     * playback of a [FeedItem] ordered by [PodXCallPromptEvent.timeStart]
     *
     * @param feedItem feed item with to be associated with [PodXCallPromptEvent] by it's
     * [FeedItem.feedUrlString]
     * @return a [Flowable] which emits the list of [PodXCallPromptEvent]s and any updates to that list
     */
    fun getCallPromptEventsForFeedItem(feedItem: FeedItem): Flowable<List<PodXCallPromptEvent>>

    /**
     * Returns a [Flowable] that emits a list of all [PodXFeedBackEvent]s that are to be shown during the
     * playback of a [FeedItem] ordered by [PodXFeedBackEvent.timeStart]
     *
     * @param feedItem feed item with to be associated with [PodXFeedBackEvent] by it's
     * [FeedItem.feedUrlString]
     * @return a [Flowable] which emits the list of [PodXFeedBackEvent]s and any updates to that list
     */
    fun getFeedBackEventsForFeedItem(feedItem: FeedItem): Flowable<List<PodXFeedBackEvent>>

    /**
     * Returns a [Flowable] that emits a list of all [PodXFeedLinkEvent]s that are to be shown during the
     * playback of a [FeedItem] ordered by [PodXFeedLinkEvent.timeStart]
     *
     * @param feedItem feed item with to be associated with [PodXFeedLinkEvent] by it's
     * [FeedItem.feedUrlString]
     * @return a [Flowable] which emits the list of [PodXFeedLinkEvent]s and any updates to that list
     */
    fun getFeedLinkEventsForFeedItem(feedItem: FeedItem): Flowable<List<PodXFeedLinkEvent>>

    /**
     * Returns a [Flowable] that emits a list of all [PodXNewsLetterSignUpEvent]s that are to be shown during the
     * playback of a [FeedItem] ordered by [PodXNewsLetterSignUpEvent.timeStart]
     *
     * @param feedItem feed item with to be associated with [PodXNewsLetterSignUpEvent] by it's
     * [FeedItem.feedUrlString]
     * @return a [Flowable] which emits the list of [PodXNewsLetterSignUpEvent]s and any updates to that list
     */
    fun getNewsLetterSignUpEventsForFeedItem(feedItem: FeedItem): Flowable<List<PodXNewsLetterSignUpEvent>>

    /**
     * Returns a [Flowable] that emits a list of all [PodXPollEvent]s that are to be shown during the
     * playback of a [FeedItem] ordered by [PodXPollEvent.timeStart]
     *
     * @param feedItem feed item with to be associated with [PodXPollEvent] by it's
     * [FeedItem.feedUrlString]
     * @return a [Flowable] which emits the list of [PodXPollEvent]s and any updates to that list
     */
    fun getPollEventsForFeedItem(feedItem: FeedItem): Flowable<List<PodXPollEvent>>

    /**
     * Returns a [Flowable] that emits a list of all [PodXSocialPromptEvent]s that are to be shown during the
     * playback of a [FeedItem] ordered by [PodXSocialPromptEvent.timeStart]
     *
     * @param feedItem feed item with to be associated with [PodXSocialPromptEvent] by it's
     * [FeedItem.feedUrlString]
     * @return a [Flowable] which emits the list of [PodXSocialPromptEvent]s and any updates to that list
     */
    fun getSocialPromptEventsForFeedItem(feedItem: FeedItem): Flowable<List<PodXSocialPromptEvent>>

    /**
     * Returns a [Flowable] that emits a list of all [PodXTextEvent]s that are to be shown during the
     * playback of a [FeedItem] ordered by [PodXTextEvent.timeStart]
     *
     * @param feedItem feed item with to be associated with [PodXTextEvent] by it's
     * [FeedItem.feedUrlString]
     * @return a [Flowable] which emits the list of [PodXTextEvent]s and any updates to that list
     */
    fun getTextEventsForFeedItem(feedItem: FeedItem): Flowable<List<PodXTextEvent>>

    /**
     * Clear all [PodXImageEvent]s associated with a [FeedItem]
     *
     * @param feedItem feed item with to be associated with [PodXImageEvent] by it's
     * [FeedItem.feedUrlString]
     */
    fun deletePodXEventsForFeedItem(feedItem: FeedItem)

    /**
     * Add a list of [PodXImageEvent]s to the repository
     *
     * @param podXImageEvents the list of events to be added
     */
    fun addPodXImageEvents(podXImageEvents: List<PodXImageEvent>)

    /**
     * Add a list of [PodXWebEvent]s to the repository
     *
     * @param podXWebEvents the list of events to be added
     */
    fun addPodXWebEvents(podXWebEvents: List<PodXWebEvent>)

    /**
     * Add a list of [PodXSupportEvent]s to the repository
     *
     * @param podXSupportEvents the list of events to be added
     */
    fun addPodXSupportEvents(podXSupportEvents: List<PodXSupportEvent>)

    /**
     * Add a list of [PodXCallPromptEvent]s to the repository
     *
     * @param podXCallPromptEvents the list of events to be added
     */
    fun addPodXCallPromptEvents(podXCallPromptEvents: List<PodXCallPromptEvent>)

    /**
     * Add a list of [PodXFeedBackEvent]s to the repository
     *
     * @param podXFeedBackEvents the list of events to be added
     */
    fun addPodXFeedBackEvents(podXFeedBackEvents: List<PodXFeedBackEvent>)

    /**
     * Add a list of [PodXFeedLinkEvent]s to the repository
     *
     * @param podXFeedLinkEvents the list of events to be added
     */
    fun addPodXFeedLinkEvents(podXFeedLinkEvents: List<PodXFeedLinkEvent>)

    /**
     * Add a list of [PodXNewsLetterSignUpEvent]s to the repository
     *
     * @param podXNewsLetterSignUpEvents the list of events to be added
     */
    fun addPodXNewsLetterSignUpEvents(podXNewsLetterSignUpEvents: List<PodXNewsLetterSignUpEvent>)

    /**
     * Add a list of [PodXPollEvent]s to the repository
     *
     * @param podXPollEvents the list of events to be added
     */
    fun addPodXPollEvents(podXPollEvents: List<PodXPollEvent>)

    /**
     * Add a list of [PodXSocialPromptEvent]s to the repository
     *
     * @param podXSocialPromptEvents the list of events to be added
     */
    fun addPodXSocialPromptEvents(podXSocialPromptEvents: List<PodXSocialPromptEvent>)

    /**
     * Add a list of [PodXTextEvent]s to the repository
     *
     * @param podXTextEvents the list of events to be added
     */
    fun addPodXTextEvents(podXTextEvents: List<PodXTextEvent>)
}