package com.guardian.core.mediaplayer.podx

import android.os.SystemClock
import android.support.v4.media.session.PlaybackStateCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.guardian.core.feeditem.FeedItem
import com.guardian.core.feeditem.FeedItemRepository
import com.guardian.core.mediaplayer.common.MediaSessionConnection
import com.guardian.core.mediaplayer.extensions.isPlaying
import com.guardian.core.podxevent.PodXCallPromptEvent
import com.guardian.core.podxevent.PodXEventRepository
import com.guardian.core.podxevent.PodXFeedBackEvent
import com.guardian.core.podxevent.PodXFeedLinkEvent
import com.guardian.core.podxevent.PodXImageEvent
import com.guardian.core.podxevent.PodXNewsLetterSignUpEvent
import com.guardian.core.podxevent.PodXPollEvent
import com.guardian.core.podxevent.PodXSocialPromptEvent
import com.guardian.core.podxevent.PodXSupportEvent
import com.guardian.core.podxevent.PodXTextEvent
import com.guardian.core.podxevent.PodXWebEvent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.max

class PodXEventEmitterImpl
@Inject constructor(
    private val mediaSessionConnection: MediaSessionConnection,
    private val podXEventRepository: PodXEventRepository,
    private val feedItemRepository: FeedItemRepository
) :
    PodXEventEmitter {

    private val currentFeedDisposable = CompositeDisposable()
    private val currentFeedItemDisposable = CompositeDisposable()

    init {
        mediaSessionConnection.playbackState.observeForever {
            registerPlaybackTimerObservable()
        }
    }

    private val podXImageEventMutableLiveData = MutableLiveData<List<PodXImageEvent>>()
        .apply {
            value = listOf()
        }
    override val podXImageEventLiveData: LiveData<List<PodXImageEvent>> =
        podXImageEventMutableLiveData

    private val podXPendingImageEventMutableLiveData = MutableLiveData<List<PodXImageEvent>>()
        .apply {
            value = listOf()
        }
    override val podXPendingImageEventLiveData: LiveData<List<PodXImageEvent>> =
        podXPendingImageEventMutableLiveData

    private val podXWebEventMutableLiveData = MutableLiveData<List<PodXWebEvent>>()
        .apply {
            value = listOf()
        }
    override val podXWebEventLiveData: LiveData<List<PodXWebEvent>> = podXWebEventMutableLiveData

    private val podXPendingWebEventMutableLiveData = MutableLiveData<List<PodXWebEvent>>()
        .apply {
            value = listOf()
        }
    override val podXPendingWebEventLiveData: LiveData<List<PodXWebEvent>> =
        podXPendingWebEventMutableLiveData

    private val podXSupportEventMutableLiveData = MutableLiveData<List<PodXSupportEvent>>()
        .apply {
            value = listOf()
        }
    override val podXSupportEventLiveData: LiveData<List<PodXSupportEvent>> =
        podXSupportEventMutableLiveData

    private val podXPendingSupportEventMutableLiveData = MutableLiveData<List<PodXSupportEvent>>()
        .apply {
            value = listOf()
        }
    override val podXPendingSupportEventLiveData: LiveData<List<PodXSupportEvent>> =
        podXPendingSupportEventMutableLiveData

    private val podXCallPromptEventMutableLiveData = MutableLiveData<List<PodXCallPromptEvent>>()
        .apply {
            value = listOf()
        }
    override val podXCallPromptEventLiveData: LiveData<List<PodXCallPromptEvent>> =
        podXCallPromptEventMutableLiveData

    private val podXPendingCallPromptEventMutableLiveData = MutableLiveData<List<PodXCallPromptEvent>>()
        .apply {
            value = listOf()
        }
    override val podXPendingCallPromptEventLiveData: LiveData<List<PodXCallPromptEvent>> =
        podXPendingCallPromptEventMutableLiveData

    private val podXFeedBackEventMutableLiveData = MutableLiveData<List<PodXFeedBackEvent>>()
        .apply {
            value = listOf()
        }
    override val podXFeedBackEventLiveData: LiveData<List<PodXFeedBackEvent>> =
        podXFeedBackEventMutableLiveData

    private val podXPendingFeedBackMutableLiveData = MutableLiveData<List<PodXFeedBackEvent>>()
        .apply {
            value = listOf()
        }
    override val podXPendingFeedBackEventLiveData: LiveData<List<PodXFeedBackEvent>> =
        podXPendingFeedBackMutableLiveData

    private val podXFeedLinkEventMutableLiveData = MutableLiveData<List<PodXFeedLinkEvent>>()
        .apply {
            value = listOf()
        }
    override val podXFeedLinkEventLiveData: LiveData<List<PodXFeedLinkEvent>> =
        podXFeedLinkEventMutableLiveData

    private val podXPendingFeedLinkEventMutableLiveData = MutableLiveData<List<PodXFeedLinkEvent>>()
        .apply {
            value = listOf()
        }
    override val podXPendingFeedLinkEventLiveData: LiveData<List<PodXFeedLinkEvent>> =
        podXPendingFeedLinkEventMutableLiveData

    private val podXNewsLetterSignUpEventMutableLiveData = MutableLiveData<List<PodXNewsLetterSignUpEvent>>()
        .apply {
            value = listOf()
        }
    override val podXNewsLetterSignUpEventLiveData: LiveData<List<PodXNewsLetterSignUpEvent>> =
        podXNewsLetterSignUpEventMutableLiveData

    private val podXPendingNewsLetterSignUpMutableLiveData = MutableLiveData<List<PodXNewsLetterSignUpEvent>>()
        .apply {
            value = listOf()
        }
    override val podXPendingNewsLetterSignUpEventLiveData: LiveData<List<PodXNewsLetterSignUpEvent>> =
        podXPendingNewsLetterSignUpMutableLiveData

    private val podXPollEventMutableLiveData = MutableLiveData<List<PodXPollEvent>>()
        .apply {
            value = listOf()
        }
    override val podXPollEventLiveData: LiveData<List<PodXPollEvent>> =
        podXPollEventMutableLiveData

    private val podXPendingPollEventMutableLiveData = MutableLiveData<List<PodXPollEvent>>()
        .apply {
            value = listOf()
        }
    override val podXPendingPollEventLiveData: LiveData<List<PodXPollEvent>> =
        podXPendingPollEventMutableLiveData

    private val podXSocialPromptEventMutableLiveData = MutableLiveData<List<PodXSocialPromptEvent>>()
        .apply {
            value = listOf()
        }
    override val podXSocialPromptEventLiveData: LiveData<List<PodXSocialPromptEvent>> =
        podXSocialPromptEventMutableLiveData

    private val podXPendingSocialPromptEventMutableLiveData = MutableLiveData<List<PodXSocialPromptEvent>>()
        .apply {
            value = listOf()
        }
    override val podXPendingSocialPromptEventLiveData: LiveData<List<PodXSocialPromptEvent>> =
        podXPendingSocialPromptEventMutableLiveData

    private val podXTextEventMutableLiveData = MutableLiveData<List<PodXTextEvent>>()
        .apply {
            value = listOf()
        }
    override val podXTextEventLiveData: LiveData<List<PodXTextEvent>> =
        podXTextEventMutableLiveData

    private val podXPendingTextEventMutableLiveData = MutableLiveData<List<PodXTextEvent>>()
        .apply {
            value = listOf()
        }
    override val podXPendingTextEventLiveData: LiveData<List<PodXTextEvent>> =
        podXPendingTextEventMutableLiveData

    private val pendingPodXImageEvents: MutableList<PodXImageEvent> = mutableListOf()
    private val pendingPodXWebEvents: MutableList<PodXWebEvent> = mutableListOf()
    private val pendingPodXSupportEvents: MutableList<PodXSupportEvent> = mutableListOf()
    private val pendingPodXCallPromptEvents: MutableList<PodXCallPromptEvent> = mutableListOf()
    private val pendingPodXFeedBackEvents: MutableList<PodXFeedBackEvent> = mutableListOf()
    private val pendingPodXFeedLinkEvents: MutableList<PodXFeedLinkEvent> = mutableListOf()
    private val pendingPodXNewsLetterSignUpEvents: MutableList<PodXNewsLetterSignUpEvent> = mutableListOf()
    private val pendingPodXPollEvents: MutableList<PodXPollEvent> = mutableListOf()
    private val pendingPodXSocialPromptEvents: MutableList<PodXSocialPromptEvent> = mutableListOf()
    private val pendingPodXTextEvents: MutableList<PodXTextEvent> = mutableListOf()

    override fun registerCurrentFeedItem(feedItem: FeedItem) {
        currentFeedDisposable.clear()
        // listen for changes on the feed item in case we need to update the episode events
        currentFeedDisposable.add(
            feedItemRepository.getFeedItemForUrlString(feedItemUrlString = feedItem.feedItemAudioUrl)
            .subscribe ({
                registerAllEventTypes(it)
            }, {
                Timber.e(it)
            })
        )

        registerAllEventTypes(feedItem)
    }

    private fun registerAllEventTypes(feedItem: FeedItem) {
        registerImageEvents(feedItem)
        registerWebEvents(feedItem)
        registerSupportEvents(feedItem)
        registerCallPromptEvents(feedItem)
        registerFeedBackEvents(feedItem)
        registerFeedLinkEvents(feedItem)
        registerNewsLetterSignUpEvents(feedItem)
        registerSocialPromptEvents(feedItem)
        registerPollEvents(feedItem)
        registerTextEvents(feedItem)
    }

    private fun registerWebEvents(feedItem: FeedItem) {
        currentFeedDisposable.add(
            podXEventRepository.getWebEventsForFeedItem(feedItem)
                .subscribe(
                    { feedPodXEventList ->
                        pendingPodXWebEvents.clear()
                        pendingPodXWebEvents.addAll(feedPodXEventList)
                        podXPendingWebEventMutableLiveData.postValue(feedPodXEventList)
                        registerPlaybackTimerObservable()
                    },
                    { e: Throwable ->
                        Timber.e(e)
                    }
                )
        )
    }

    private fun registerImageEvents(feedItem: FeedItem) {
        currentFeedDisposable.add(
            podXEventRepository.getImageEventsForFeedItem(feedItem)
                .subscribe(
                    { feedPodXEventList ->
                        pendingPodXImageEvents.clear()
                        pendingPodXImageEvents.addAll(feedPodXEventList)
                        podXPendingImageEventMutableLiveData.postValue(feedPodXEventList)
                        registerPlaybackTimerObservable()
                    },
                    { e: Throwable ->
                        Timber.e(e)
                    }
                )
        )
    }

    private fun registerSupportEvents(feedItem: FeedItem) {
        currentFeedDisposable.add(
            podXEventRepository.getSupportEventsForFeedItem(feedItem)
                .subscribe(
                    { feedPodXEventList ->
                        Timber.i("we have ${feedPodXEventList.size} elements coming from cache")
                        pendingPodXSupportEvents.clear()
                        pendingPodXSupportEvents.addAll(feedPodXEventList)
                        podXPendingSupportEventMutableLiveData.postValue(feedPodXEventList)
                        registerPlaybackTimerObservable()
                    },
                    { e: Throwable ->
                        Timber.e(e)
                    }
                )
        )
    }

    private fun registerCallPromptEvents(feedItem: FeedItem) {
        currentFeedDisposable.add(
            podXEventRepository.getCallPromptEventsForFeedItem(feedItem)
                .subscribe(
                    { feedPodXEventList ->
                        Timber.i("call prompts emitting ${feedPodXEventList.firstOrNull()?.phoneNumber}")
                        Timber.i("call prompts emitting ${feedPodXEventList.firstOrNull()?.phoneNumber}")
                        pendingPodXCallPromptEvents.clear()
                        pendingPodXCallPromptEvents.addAll(feedPodXEventList)
                        podXPendingCallPromptEventMutableLiveData.postValue(feedPodXEventList)
                        registerPlaybackTimerObservable()
                    },
                    { e: Throwable ->
                        Timber.e(e)
                    }
                )
        )
    }

    private fun registerFeedBackEvents(feedItem: FeedItem) {
        currentFeedDisposable.add(
            podXEventRepository.getFeedBackEventsForFeedItem(feedItem)
                .subscribe(
                    { feedPodXEventList ->
                        pendingPodXFeedBackEvents.clear()
                        pendingPodXFeedBackEvents.addAll(feedPodXEventList)
                        podXPendingFeedBackMutableLiveData.postValue(feedPodXEventList)
                        registerPlaybackTimerObservable()
                    },
                    { e: Throwable ->
                        Timber.e(e)
                    }
                )
        )
    }

    private fun registerFeedLinkEvents(feedItem: FeedItem) {
        currentFeedDisposable.add(
            podXEventRepository.getFeedLinkEventsForFeedItem(feedItem)
                .subscribe(
                    { feedPodXEventList ->
                        pendingPodXFeedLinkEvents.clear()
                        pendingPodXFeedLinkEvents.addAll(feedPodXEventList)
                        podXPendingFeedLinkEventMutableLiveData.postValue(feedPodXEventList)
                        registerPlaybackTimerObservable()
                    },
                    { e: Throwable ->
                        Timber.e(e)
                    }
                )
        )
    }

    private fun registerNewsLetterSignUpEvents(feedItem: FeedItem) {
        currentFeedDisposable.add(
            podXEventRepository.getNewsLetterSignUpEventsForFeedItem(feedItem)
                .subscribe(
                    { feedPodXEventList ->
                        pendingPodXNewsLetterSignUpEvents.clear()
                        pendingPodXNewsLetterSignUpEvents.addAll(feedPodXEventList)
                        podXPendingNewsLetterSignUpMutableLiveData.postValue(feedPodXEventList)
                        registerPlaybackTimerObservable()
                    },
                    { e: Throwable ->
                        Timber.e(e)
                    }
                )
        )
    }

    private fun registerPollEvents(feedItem: FeedItem) {
        currentFeedDisposable.add(
            podXEventRepository.getPollEventsForFeedItem(feedItem)
                .subscribe(
                    { feedPodXEventList ->
                        pendingPodXPollEvents.clear()
                        pendingPodXPollEvents.addAll(feedPodXEventList)
                        podXPendingPollEventMutableLiveData.postValue(feedPodXEventList)
                        registerPlaybackTimerObservable()
                    },
                    { e: Throwable ->
                        Timber.e(e)
                    }
                )
        )
    }

    private fun registerSocialPromptEvents(feedItem: FeedItem) {
        currentFeedDisposable.add(
            podXEventRepository.getSocialPromptEventsForFeedItem(feedItem)
                .subscribe(
                    { feedPodXEventList ->
                        pendingPodXSocialPromptEvents.clear()
                        pendingPodXSocialPromptEvents.addAll(feedPodXEventList)
                        podXPendingSocialPromptEventMutableLiveData.postValue(feedPodXEventList)
                        registerPlaybackTimerObservable()
                    },
                    { e: Throwable ->
                        Timber.e(e)
                    }
                )
        )
    }

    private fun registerTextEvents(feedItem: FeedItem) {
        currentFeedDisposable.add(
            podXEventRepository.getTextEventsForFeedItem(feedItem)
                .subscribe(
                    { feedPodXEventList ->
                        pendingPodXTextEvents.clear()
                        pendingPodXTextEvents.addAll(feedPodXEventList)
                        podXPendingTextEventMutableLiveData.postValue(feedPodXEventList)
                        registerPlaybackTimerObservable()
                    },
                    { e: Throwable ->
                        Timber.e(e)
                    }
                )
        )
    }

    private fun registerPlaybackTimerObservable() {
        currentFeedItemDisposable.clear()
        val playbackState = mediaSessionConnection.playbackState.value
        if (playbackState != null && playbackState.isPlaying) {
            val currentTimeMillis = getPlaybackPositionFromState(playbackState)
            updateEmittedEvents(currentTimeMillis)

            val nextEventTime = getNextEventTime(currentTimeMillis)
            val nextEventObservable = Observable.fromCallable {
                mediaSessionConnection.playbackState.value.let { playbackState ->
                    if (playbackState != null) {
                        getPlaybackPositionFromState(playbackState)
                    } else {
                        0L
                    }
                }
            }.delaySubscription(
                nextEventTime - currentTimeMillis, TimeUnit.MILLISECONDS
            )

            currentFeedItemDisposable.add(
                nextEventObservable
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            registerPlaybackTimerObservable()
                        },
                        { e: Throwable? ->
                            Timber.e(e)
                        }
                    )
            )
        }
    }

    private fun getNextEventTime(currentTimeMillis: Long): Long {
        val minEvent = (
            pendingPodXImageEvents.flatMap {
                listOf(it.timeStart, it.timeEnd)
            } + pendingPodXCallPromptEvents.flatMap {
                listOf(it.timeStart, it.timeEnd)
            } + pendingPodXFeedBackEvents.flatMap {
                listOf(it.timeStart, it.timeEnd)
            } + pendingPodXFeedLinkEvents.flatMap {
                listOf(it.timeStart, it.timeEnd)
            } + pendingPodXNewsLetterSignUpEvents.flatMap {
                listOf(it.timeStart, it.timeEnd)
            } + pendingPodXPollEvents.flatMap {
                listOf(it.timeStart, it.timeEnd)
            } + pendingPodXSocialPromptEvents.flatMap {
                listOf(it.timeStart, it.timeEnd)
            } + pendingPodXSupportEvents.flatMap {
                listOf(it.timeStart, it.timeEnd)
            } + pendingPodXTextEvents.flatMap {
                listOf(it.timeStart, it.timeEnd)
            } + pendingPodXWebEvents.flatMap {
                listOf(it.timeStart, it.timeEnd)
            }
            ).filter {
            eventTime ->
            eventTime >= currentTimeMillis
        }.minOrNull() ?: Long.MAX_VALUE - currentTimeMillis

        Timber.i("next event timer ${max(minEvent, 250L)}")
        Timber.i("next event timer current $currentTimeMillis")
        return max(minEvent, 250L)
    }

    private fun updateEmittedEvents(timeMillis: Long) {
        val currentImageEventList = pendingPodXImageEvents
            .filter { pendingImageEvent ->
                pendingImageEvent.timeStart <= timeMillis &&
                    pendingImageEvent.timeEnd >= timeMillis
            }
        // only post if there are new values
        if (currentImageEventList
            .size != (podXImageEventMutableLiveData.value)?.size ?: 0
        ) {
            for (imageEvent in currentImageEventList) {
                Timber.i("web event id ${imageEvent.id}")
            }
            podXImageEventMutableLiveData.postValue(currentImageEventList)
        }

        val currentWebEventList = pendingPodXWebEvents
            .filter { pendingWebEvent ->
                pendingWebEvent.timeStart <= timeMillis &&
                    pendingWebEvent.timeEnd >= timeMillis
            }
        if (currentWebEventList
            .size != (podXWebEventMutableLiveData.value)?.size ?: 0
        ) {
            for (webEvent in currentWebEventList) {
                Timber.i("web event id ${webEvent.id}")
            }
            podXWebEventMutableLiveData.postValue(currentWebEventList)
        }

        val currentSupportEventList = pendingPodXSupportEvents
            .filter { pendingSupportEvent ->
                pendingSupportEvent.timeStart <= timeMillis &&
                    pendingSupportEvent.timeEnd >= timeMillis
            }
        podXSupportEventMutableLiveData.postValue(currentSupportEventList)
        if (currentSupportEventList
            .size != (podXSupportEventMutableLiveData.value)?.size ?: 0
        ) {
            podXSupportEventMutableLiveData.postValue(currentSupportEventList)
        }

        val currentCallPromptEventList = pendingPodXCallPromptEvents
            .filter { pendingCallPromptEvent ->
                pendingCallPromptEvent.timeStart <= timeMillis &&
                    pendingCallPromptEvent.timeEnd >= timeMillis
            }
        podXCallPromptEventMutableLiveData.postValue(currentCallPromptEventList)
        if (currentCallPromptEventList
            .size != (podXCallPromptEventMutableLiveData.value)?.size ?: 0
        ) {
            podXCallPromptEventMutableLiveData.postValue(currentCallPromptEventList)
        }

        val currentFeedBackEventList = pendingPodXFeedBackEvents
            .filter { pendingFeedBackEvent ->
                pendingFeedBackEvent.timeStart <= timeMillis &&
                    pendingFeedBackEvent.timeEnd >= timeMillis
            }
        podXFeedBackEventMutableLiveData.postValue(currentFeedBackEventList)
        if (currentFeedBackEventList
            .size != (podXFeedBackEventMutableLiveData.value)?.size ?: 0
        ) {
            podXFeedBackEventMutableLiveData.postValue(currentFeedBackEventList)
        }

        val currentFeedLinkEventList = pendingPodXFeedLinkEvents
            .filter { pendingFeedLinkEvent ->
                pendingFeedLinkEvent.timeStart <= timeMillis &&
                    pendingFeedLinkEvent.timeEnd >= timeMillis
            }
        podXFeedLinkEventMutableLiveData.postValue(currentFeedLinkEventList)
        if (currentFeedLinkEventList
            .size != (podXFeedLinkEventMutableLiveData.value)?.size ?: 0
        ) {
            Timber.i("feed link count: ${currentFeedLinkEventList.size}")
            podXFeedLinkEventMutableLiveData.postValue(currentFeedLinkEventList)
        }

        val currentNewsLetterSignUpEventList = pendingPodXNewsLetterSignUpEvents
            .filter { pendingNewsLetterSignUpEvent ->
                pendingNewsLetterSignUpEvent.timeStart <= timeMillis &&
                    pendingNewsLetterSignUpEvent.timeEnd >= timeMillis
            }
        podXNewsLetterSignUpEventMutableLiveData.postValue(currentNewsLetterSignUpEventList)
        if (currentNewsLetterSignUpEventList
            .size != (podXNewsLetterSignUpEventMutableLiveData.value)?.size ?: 0
        ) {
            podXNewsLetterSignUpEventMutableLiveData.postValue(currentNewsLetterSignUpEventList)
        }

        val currentSocialPromptEventList = pendingPodXSocialPromptEvents
            .filter { pendingSocialPromptEvent ->
                pendingSocialPromptEvent.timeStart <= timeMillis &&
                    pendingSocialPromptEvent.timeEnd >= timeMillis
            }
        podXSocialPromptEventMutableLiveData.postValue(currentSocialPromptEventList)
        if (currentSocialPromptEventList
            .size != (podXSocialPromptEventMutableLiveData.value)?.size ?: 0
        ) {
            podXSocialPromptEventMutableLiveData.postValue(currentSocialPromptEventList)
        }

        val currentTextEventList = pendingPodXTextEvents
            .filter { pendingTextEvent ->
                pendingTextEvent.timeStart <= timeMillis &&
                    pendingTextEvent.timeEnd >= timeMillis
            }
        podXTextEventMutableLiveData.postValue(currentTextEventList)
        if (currentTextEventList
            .size != (podXTextEventMutableLiveData.value)?.size ?: 0
        ) {
            podXTextEventMutableLiveData.postValue(currentTextEventList)
        }

        val currentPollEventList = pendingPodXPollEvents
            .filter { pendingPollEvent ->
                pendingPollEvent.timeStart <= timeMillis &&
                    pendingPollEvent.timeEnd >= timeMillis
            }
        podXPollEventMutableLiveData.postValue(currentPollEventList)
        if (currentPollEventList
            .size != (podXPollEventMutableLiveData.value)?.size ?: 0
        ) {
            podXPollEventMutableLiveData.postValue(currentPollEventList)
        }
    }

    private fun getPlaybackPositionFromState(playbackStateCompat: PlaybackStateCompat): Long {
        return if (playbackStateCompat.state == PlaybackStateCompat.STATE_PLAYING) {
            val timeDelta = SystemClock.elapsedRealtime() -
                playbackStateCompat.lastPositionUpdateTime

            (playbackStateCompat.position + (timeDelta * playbackStateCompat.playbackSpeed))
                .toLong()
        } else {
            playbackStateCompat.position
        }
    }
}
