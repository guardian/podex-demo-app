package com.guardian.podx.service.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.guardian.core.mediaplayer.podx.PodXEventEmitter
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
import com.guardian.podx.R
import dagger.android.AndroidInjection
import timber.log.Timber
import javax.inject.Inject

class EventNotificationService : Service(){
    @Inject
    lateinit var podXEventEmitter: PodXEventEmitter

    private val defaultArgsBundle = Bundle().apply{
        putBoolean("scrollToEvents", true)
    }

    private val notificationManager: NotificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onCreate() {
        super.onCreate()
        AndroidInjection.inject(this)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !eventChannelExists()) {
            createEventChannel()
        }

        podXEventEmitter.podXImageEventLiveData
            .observeForever { imageEventList ->
                Timber.i("getting event list")
                displayImageEvents(imageEventList)
            }

        podXEventEmitter.podXCallPromptEventLiveData
            .observeForever { callPromptEventList ->
                displayCallPromptEvents(callPromptEventList)
            }

        podXEventEmitter.podXFeedBackEventLiveData
            .observeForever { feedBackEventList ->
                displayFeedBackEvents(feedBackEventList)
            }

        podXEventEmitter.podXFeedLinkEventLiveData
            .observeForever { feedLinkEventList ->
                displayFeedLinkEvents(feedLinkEventList)
            }

        podXEventEmitter.podXNewsLetterSignUpEventLiveData
            .observeForever{ newsLetterSignUpEventList ->
                displayNewsLetterSignUpEvents(newsLetterSignUpEventList)
            }

        podXEventEmitter.podXPollEventLiveData
            .observeForever{ pollEventList ->
                displayPollEvents(pollEventList)
            }

        podXEventEmitter.podXSocialPromptEventLiveData
            .observeForever{ socialPromptEventList ->
                displaySocialPromptEvents(socialPromptEventList)
            }

        podXEventEmitter.podXSupportEventLiveData
            .observeForever{ supportEventList ->
                displaySupportEvents(supportEventList)
            }

        podXEventEmitter.podXTextEventLiveData
            .observeForever{ textEventList ->
                displayTextEvents(textEventList)
            }

        podXEventEmitter.podXWebEventLiveData
            .observeForever{ webEventList ->
                displayWebEvents(webEventList)
            }


        return super.onStartCommand(intent, flags, startId)
    }

    private val displayingImageIds = mutableListOf<Int>()
    private fun displayImageEvents(imageEventList: List<PodXImageEvent>) {
        val newAggregate = displayingImageIds.size == 0 && imageEventList.isNotEmpty()

        //remove notifications that aren't coming back from the emitter anymore
        displayingImageIds.filter {
            it !in imageEventList.map { image ->
                image.id % PODX_IMAGE_NOTIFICATION_RANGE +
                PODX_IMAGE_NOTIFICATION_RANGE }
        }.forEach{
            notificationManager.cancel(it)
        }

        imageEventList.forEach { imageEvent ->
            val notificationId = imageEvent.id % PODX_IMAGE_NOTIFICATION_RANGE +
                PODX_IMAGE_NOTIFICATION_RANGE
            if (notificationId !in displayingImageIds) {
                displayingImageIds.add(notificationId)
                val imagePendingIntent = NavDeepLinkBuilder(this)
                    .setGraph(R.navigation.navgraph_main)
                    .setDestination(R.id.playerFragment)
                    .setArguments(defaultArgsBundle)
                    .createPendingIntent()

                val notification = NotificationCompat.Builder(this, PODX_EVENT_CHANNEL)
                    .setContentText(imageEvent.notification)
                    .setSmallIcon(R.drawable.ic_icons_image)
                    .setContentIntent(imagePendingIntent)
                    .setGroup(PODX_IMAGE_GROUP_KEY)
                    .build()

                notificationManager.notify(notificationId, notification)
            }
        }

        if (newAggregate) {
            val contentString = if (imageEventList.size > 1) {
                "${imageEventList.size} " +  getString(R.string.notification_image_content_plural)
            } else {
                "${imageEventList.size} " + getString(R.string.notification_image_content_singular)
            }
            val aggregateImageNotification = NotificationCompat.Builder(this, PODX_EVENT_CHANNEL)
                .setContentTitle(getString(R.string.notification_image_title))
                .setContentText(contentString)
                .setSmallIcon(R.drawable.ic_icons_image)
                .setGroup(PODX_IMAGE_GROUP_KEY)
                .setGroupSummary(true)
                .build()

            notificationManager.notify(PODX_IMAGE_AGGREGATE_ID, aggregateImageNotification)
        } else {
            if (imageEventList.isEmpty()) {
                notificationManager.cancel(PODX_IMAGE_AGGREGATE_ID)
            }
        }
    }

    private val displayingCallPromptIds = mutableListOf<Int>()
    private fun displayCallPromptEvents(callPromptEventList: List<PodXCallPromptEvent>) {
        val newAggregate = displayingCallPromptIds.size == 0 && callPromptEventList.isNotEmpty()

        //remove notifications that aren't coming back from the emitter anymore
        displayingCallPromptIds.filter {
            it !in callPromptEventList.map { callPrompt ->
                callPrompt.id % PODX_CALL_PROMPT_NOTIFICATION_RANGE +
                    PODX_CALL_PROMPT_NOTIFICATION_RANGE }
        }.forEach{
            notificationManager.cancel(it)
        }

        callPromptEventList.forEach { callPromptEvent ->
            val notificationId = callPromptEvent.id % PODX_CALL_PROMPT_NOTIFICATION_RANGE +
                PODX_CALL_PROMPT_NOTIFICATION_RANGE
            if (notificationId !in displayingCallPromptIds) {
                displayingCallPromptIds.add(notificationId)
                val callPromptPendingIntent = NavDeepLinkBuilder(this)
                    .setGraph(R.navigation.navgraph_main)
                    .setDestination(R.id.playerFragment)
                    .setArguments(defaultArgsBundle)
                    .createPendingIntent()

                val notification = NotificationCompat.Builder(this, PODX_EVENT_CHANNEL)
                    .setContentText(callPromptEvent.notification)
                    .setSmallIcon(R.drawable.ic_icons_call)
                    .setContentIntent(callPromptPendingIntent)
                    .setGroup(PODX_CALL_PROMPT_GROUP_KEY)
                    .build()

                notificationManager.notify(notificationId, notification)
            }
        }

        if (newAggregate) {
            val contentString = if (callPromptEventList.size > 1) {
                "${callPromptEventList.size} " +  getString(R.string.notification_callprompt_content_plural)
            } else {
                "${callPromptEventList.size} " + getString(R.string.notification_callprompt_content_singular)
            }
            val aggregateCallPromptNotification = NotificationCompat.Builder(this, PODX_EVENT_CHANNEL)
                .setContentTitle(getString(R.string.notification_callprompt_title))
                .setContentText(contentString)
                .setSmallIcon(R.drawable.ic_icons_call)
                .setGroup(PODX_CALL_PROMPT_GROUP_KEY)
                .setGroupSummary(true)
                .build()

            notificationManager.notify(PODX_CALL_PROMPT_AGGREGATE_ID, aggregateCallPromptNotification)
        } else {
            if (callPromptEventList.isEmpty()) {
                notificationManager.cancel(PODX_CALL_PROMPT_AGGREGATE_ID)
            }
        }
    }

    private val displayingFeedBackIds = mutableListOf<Int>()
    private fun displayFeedBackEvents(feedBackEventList: List<PodXFeedBackEvent>) {
        val newAggregate = displayingFeedBackIds.size == 0 && feedBackEventList.isNotEmpty()

        //remove notifications that aren't coming back from the emitter anymore
        displayingFeedBackIds.filter {
            it !in feedBackEventList.map { feedBack ->
                feedBack.id % PODX_FEED_BACK_NOTIFICATION_RANGE +
                    PODX_FEED_BACK_NOTIFICATION_RANGE }
        }.forEach{
            notificationManager.cancel(it)
        }

        feedBackEventList.forEach { feedBackEvent ->
            val notificationId = feedBackEvent.id % PODX_FEED_BACK_NOTIFICATION_RANGE +
                PODX_FEED_BACK_NOTIFICATION_RANGE
            if (notificationId !in displayingFeedBackIds) {
                displayingFeedBackIds.add(notificationId)
                val feedBackPendingIntent = NavDeepLinkBuilder(this)
                    .setGraph(R.navigation.navgraph_main)
                    .setDestination(R.id.playerFragment)
                    .setArguments(defaultArgsBundle)
                    .createPendingIntent()

                val notification = NotificationCompat.Builder(this, PODX_EVENT_CHANNEL)
                    .setContentText(feedBackEvent.notification)
                    .setSmallIcon(R.drawable.ic_icons_feedback)
                    .setContentIntent(feedBackPendingIntent)
                    .setGroup(PODX_FEED_BACK_GROUP_KEY)
                    .build()

                notificationManager.notify(notificationId, notification)
            }
        }

        if (newAggregate) {
            val contentString = if (feedBackEventList.size > 1) {
                "${feedBackEventList.size} " +  getString(R.string.notification_feed_back_content_plural)
            } else {
                "${feedBackEventList.size} " + getString(R.string.notification_feed_back_content_singular)
            }
            val aggregateFeedBackNotification = NotificationCompat.Builder(this, PODX_EVENT_CHANNEL)
                .setContentTitle(getString(R.string.notification_feed_back_title))
                .setContentText(contentString)
                .setSmallIcon(R.drawable.ic_icons_feedback)
                .setGroup(PODX_FEED_BACK_GROUP_KEY)
                .setGroupSummary(true)
                .build()

            notificationManager.notify(PODX_FEED_BACK_AGGREGATE_ID, aggregateFeedBackNotification)
        } else {
            if (feedBackEventList.isEmpty()) {
                notificationManager.cancel(PODX_FEED_BACK_AGGREGATE_ID)
            }
        }
    }

    private val displayingFeedLinkIds = mutableListOf<Int>()
    private fun displayFeedLinkEvents(FeedLinkEventList: List<PodXFeedLinkEvent>) {
        val newAggregate = displayingFeedLinkIds.size == 0 && FeedLinkEventList.isNotEmpty()

        //remove notifications that aren't coming back from the emitter anymore
        displayingFeedLinkIds.filter {
            it !in FeedLinkEventList.map { feedLink ->
                feedLink.id % PODX_FEED_LINK_NOTIFICATION_RANGE +
                    PODX_FEED_LINK_NOTIFICATION_RANGE }
        }.forEach{
            notificationManager.cancel(it)
        }

        FeedLinkEventList.forEach { feedLinkEvent ->
            val notificationId = feedLinkEvent.id % PODX_FEED_LINK_NOTIFICATION_RANGE +
                PODX_FEED_LINK_NOTIFICATION_RANGE
            if (notificationId !in displayingFeedLinkIds) {
                displayingFeedLinkIds.add(notificationId)
                val feedLinkPendingIntent = NavDeepLinkBuilder(this)
                    .setGraph(R.navigation.navgraph_main)
                    .setDestination(R.id.playerFragment)
                    .setArguments(defaultArgsBundle)
                    .createPendingIntent()

                val notification = NotificationCompat.Builder(this, PODX_EVENT_CHANNEL)
                    .setContentText(feedLinkEvent.notification)
                    .setSmallIcon(R.drawable.ic_icons_podcast)
                    .setContentIntent(feedLinkPendingIntent)
                    .setGroup(PODX_FEED_LINK_GROUP_KEY)
                    .build()

                notificationManager.notify(notificationId, notification)
            }
        }

        if (newAggregate) {
            val contentString = if (FeedLinkEventList.size > 1) {
                "${FeedLinkEventList.size} " +  getString(R.string.notification_feed_link_content_plural)
            } else {
                "${FeedLinkEventList.size} " + getString(R.string.notification_feed_link_content_singular)
            }
            val aggregateFeedLinkNotification = NotificationCompat.Builder(this, PODX_EVENT_CHANNEL)
                .setContentTitle(getString(R.string.notification_feed_link_title))
                .setContentText(contentString)
                .setSmallIcon(R.drawable.ic_icons_podcast)
                .setGroup(PODX_FEED_LINK_GROUP_KEY)
                .setGroupSummary(true)
                .build()

            notificationManager.notify(PODX_FEED_LINK_AGGREGATE_ID, aggregateFeedLinkNotification)
        } else {
            if (FeedLinkEventList.isEmpty()) {
                notificationManager.cancel(PODX_FEED_LINK_AGGREGATE_ID)
            }
        }
    }

    private val displayingNewsLetterSignUpIds = mutableListOf<Int>()
    private fun displayNewsLetterSignUpEvents(NewsLetterSignUpEventList: List<PodXNewsLetterSignUpEvent>) {
        val newAggregate = displayingNewsLetterSignUpIds.size == 0 && NewsLetterSignUpEventList.isNotEmpty()

        //remove notifications that aren't coming back from the emitter anymore
        displayingNewsLetterSignUpIds.filter {
            it !in NewsLetterSignUpEventList.map { newsLetterSignUp ->
                newsLetterSignUp.id % PODX_NEWS_LETTER_SIGN_UP_NOTIFICATION_RANGE +
                    PODX_NEWS_LETTER_SIGN_UP_NOTIFICATION_RANGE }
        }.forEach{
            notificationManager.cancel(it)
        }

        NewsLetterSignUpEventList.forEach { newsLetterSignUpEvent ->
            val notificationId = newsLetterSignUpEvent.id % PODX_NEWS_LETTER_SIGN_UP_NOTIFICATION_RANGE +
                PODX_NEWS_LETTER_SIGN_UP_NOTIFICATION_RANGE
            if (notificationId !in displayingNewsLetterSignUpIds) {
                displayingNewsLetterSignUpIds.add(notificationId)
                val newsLetterSignUpPendingIntent = NavDeepLinkBuilder(this)
                    .setGraph(R.navigation.navgraph_main)
                    .setDestination(R.id.playerFragment)
                    .setArguments(defaultArgsBundle)
                    .createPendingIntent()

                val notification = NotificationCompat.Builder(this, PODX_EVENT_CHANNEL)
                    .setContentText(newsLetterSignUpEvent.notification)
                    .setSmallIcon(R.drawable.ic_icons_newsletter)
                    .setContentIntent(newsLetterSignUpPendingIntent)
                    .setGroup(PODX_NEWS_LETTER_SIGN_UP_GROUP_KEY)
                    .build()

                notificationManager.notify(notificationId, notification)
            }
        }

        if (newAggregate) {
            val contentString = if (NewsLetterSignUpEventList.size > 1) {
                "${NewsLetterSignUpEventList.size} " +  getString(R.string.notification_news_letter_sign_up_content_plural)
            } else {
                "${NewsLetterSignUpEventList.size} " + getString(R.string.notification_news_letter_sign_up_content_singular)
            }
            val aggregateNewsLetterSignUpNotification = NotificationCompat.Builder(this, PODX_EVENT_CHANNEL)
                .setContentTitle(getString(R.string.notification_news_letter_sign_up_title))
                .setContentText(contentString)
                .setSmallIcon(R.drawable.ic_icons_newsletter)
                .setGroup(PODX_NEWS_LETTER_SIGN_UP_GROUP_KEY)
                .setGroupSummary(true)
                .build()

            notificationManager.notify(PODX_NEWS_LETTER_SIGN_UP_AGGREGATE_ID, aggregateNewsLetterSignUpNotification)
        } else {
            if (NewsLetterSignUpEventList.isEmpty()) {
                notificationManager.cancel(PODX_NEWS_LETTER_SIGN_UP_AGGREGATE_ID)
            }
        }
    }

    private val displayingPollIds = mutableListOf<Int>()
    private fun displayPollEvents(PollEventList: List<PodXPollEvent>) {
        val newAggregate = displayingPollIds.size == 0 && PollEventList.isNotEmpty()

        //remove notifications that aren't coming back from the emitter anymore
        displayingPollIds.filter {
            it !in PollEventList.map { poll ->
                poll.id % PODX_POLL_NOTIFICATION_RANGE +
                    PODX_POLL_NOTIFICATION_RANGE }
        }.forEach{
            notificationManager.cancel(it)
        }

        PollEventList.forEach { pollEvent ->
            val notificationId = pollEvent.id % PODX_POLL_NOTIFICATION_RANGE +
                PODX_POLL_NOTIFICATION_RANGE
            if (notificationId !in displayingPollIds) {
                displayingPollIds.add(notificationId)
                val pollPendingIntent = NavDeepLinkBuilder(this)
                    .setGraph(R.navigation.navgraph_main)
                    .setDestination(R.id.playerFragment)
                    .setArguments(defaultArgsBundle)
                    .createPendingIntent()

                val notification = NotificationCompat.Builder(this, PODX_EVENT_CHANNEL)
                    .setContentText(pollEvent.notification)
                    .setSmallIcon(R.drawable.ic_icons_poll)
                    .setContentIntent(pollPendingIntent)
                    .setGroup(PODX_POLL_GROUP_KEY)
                    .build()

                notificationManager.notify(notificationId, notification)
            }
        }

        if (newAggregate) {
            val contentString = if (PollEventList.size > 1) {
                "${PollEventList.size} " +  getString(R.string.notification_poll_content_plural)
            } else {
                "${PollEventList.size} " + getString(R.string.notification_poll_content_singular)
            }
            val aggregatePollNotification = NotificationCompat.Builder(this, PODX_EVENT_CHANNEL)
                .setContentTitle(getString(R.string.notification_poll_title))
                .setContentText(contentString)
                .setSmallIcon(R.drawable.ic_icons_poll)
                .setGroup(PODX_POLL_GROUP_KEY)
                .setGroupSummary(true)
                .build()

            notificationManager.notify(PODX_POLL_AGGREGATE_ID, aggregatePollNotification)
        } else {
            if (PollEventList.isEmpty()) {
                notificationManager.cancel(PODX_POLL_AGGREGATE_ID)
            }
        }
    }

    private val displayingSocialPromptIds = mutableListOf<Int>()
    private fun displaySocialPromptEvents(SocialPromptEventList: List<PodXSocialPromptEvent>) {
        val newAggregate = displayingSocialPromptIds.size == 0 && SocialPromptEventList.isNotEmpty()

        //remove notifications that aren't coming back from the emitter anymore
        displayingSocialPromptIds.filter {
            it !in SocialPromptEventList.map { socialPrompt ->
                socialPrompt.id % PODX_SOCIAL_PROMPT_NOTIFICATION_RANGE +
                    PODX_SOCIAL_PROMPT_NOTIFICATION_RANGE }
        }.forEach{
            notificationManager.cancel(it)
        }

        SocialPromptEventList.forEach { socialPromptEvent ->
            val notificationId = socialPromptEvent.id % PODX_SOCIAL_PROMPT_NOTIFICATION_RANGE +
                PODX_SOCIAL_PROMPT_NOTIFICATION_RANGE
            if (notificationId !in displayingSocialPromptIds) {
                displayingSocialPromptIds.add(notificationId)
                val socialPromptPendingIntent = NavDeepLinkBuilder(this)
                    .setGraph(R.navigation.navgraph_main)
                    .setDestination(R.id.playerFragment)
                    .setArguments(defaultArgsBundle)
                    .createPendingIntent()

                val notification = NotificationCompat.Builder(this, PODX_EVENT_CHANNEL)
                    .setContentText(socialPromptEvent.notification)
                    .setSmallIcon(R.drawable.ic_icons_social)
                    .setContentIntent(socialPromptPendingIntent)
                    .setGroup(PODX_SOCIAL_PROMPT_GROUP_KEY)
                    .build()

                notificationManager.notify(notificationId, notification)
            }
        }

        if (newAggregate) {
            val contentString = if (SocialPromptEventList.size > 1) {
                "${SocialPromptEventList.size} " +  getString(R.string.notification_social_prompt_content_plural)
            } else {
                "${SocialPromptEventList.size} " + getString(R.string.notification_social_prompt_content_singular)
            }
            val aggregateSocialPromptNotification = NotificationCompat.Builder(this, PODX_EVENT_CHANNEL)
                .setContentTitle(getString(R.string.notification_social_prompt_title))
                .setContentText(contentString)
                .setSmallIcon(R.drawable.ic_icons_social)
                .setGroup(PODX_SOCIAL_PROMPT_GROUP_KEY)
                .setGroupSummary(true)
                .build()

            notificationManager.notify(PODX_SOCIAL_PROMPT_AGGREGATE_ID, aggregateSocialPromptNotification)
        } else {
            if (SocialPromptEventList.isEmpty()) {
                notificationManager.cancel(PODX_SOCIAL_PROMPT_AGGREGATE_ID)
            }
        }
    }

    private val displayingSupportIds = mutableListOf<Int>()
    private fun displaySupportEvents(SupportEventList: List<PodXSupportEvent>) {
        val newAggregate = displayingSupportIds.size == 0 && SupportEventList.isNotEmpty()

        //remove notifications that aren't coming back from the emitter anymore
        displayingSupportIds.filter {
            it !in SupportEventList.map { support ->
                support.id % PODX_SUPPORT_NOTIFICATION_RANGE +
                    PODX_SUPPORT_NOTIFICATION_RANGE }
        }.forEach{
            notificationManager.cancel(it)
        }

        SupportEventList.forEach { supportEvent ->
            val notificationId = supportEvent.id % PODX_SUPPORT_NOTIFICATION_RANGE +
                PODX_SUPPORT_NOTIFICATION_RANGE
            if (notificationId !in displayingSupportIds) {
                displayingSupportIds.add(notificationId)
                val supportPendingIntent = NavDeepLinkBuilder(this)
                    .setGraph(R.navigation.navgraph_main)
                    .setDestination(R.id.playerFragment)
                    .setArguments(defaultArgsBundle)
                    .createPendingIntent()

                val notification = NotificationCompat.Builder(this, PODX_EVENT_CHANNEL)
                    .setContentText(supportEvent.notification)
                    .setSmallIcon(R.drawable.ic_icons_link)
                    .setContentIntent(supportPendingIntent)
                    .setGroup(PODX_SUPPORT_GROUP_KEY)
                    .build()

                notificationManager.notify(notificationId, notification)
            }
        }

        if (newAggregate) {
            val contentString = if (SupportEventList.size > 1) {
                "${SupportEventList.size} " +  getString(R.string.notification_support_content_plural)
            } else {
                "${SupportEventList.size} " + getString(R.string.notification_support_content_singular)
            }
            val aggregateSupportNotification = NotificationCompat.Builder(this, PODX_EVENT_CHANNEL)
                .setContentTitle(getString(R.string.notification_support_title))
                .setContentText(contentString)
                .setSmallIcon(R.drawable.ic_icons_link)
                .setGroup(PODX_SUPPORT_GROUP_KEY)
                .setGroupSummary(true)
                .build()

            notificationManager.notify(PODX_SUPPORT_AGGREGATE_ID, aggregateSupportNotification)
        } else {
            if (SupportEventList.isEmpty()) {
                notificationManager.cancel(PODX_SUPPORT_AGGREGATE_ID)
            }
        }
    }

    private val displayingTextIds = mutableListOf<Int>()
    private fun displayTextEvents(TextEventList: List<PodXTextEvent>) {
        val newAggregate = displayingTextIds.size == 0 && TextEventList.isNotEmpty()

        //remove notifications that aren't coming back from the emitter anymore
        displayingTextIds.filter {
            it !in TextEventList.map { text ->
                text.id % PODX_TEXT_NOTIFICATION_RANGE +
                    PODX_TEXT_NOTIFICATION_RANGE }
        }.forEach{
            notificationManager.cancel(it)
        }

        TextEventList.forEach { textEvent ->
            val notificationId = textEvent.id % PODX_TEXT_NOTIFICATION_RANGE +
                PODX_TEXT_NOTIFICATION_RANGE
            if (notificationId !in displayingTextIds) {
                displayingTextIds.add(notificationId)
                val textPendingIntent = NavDeepLinkBuilder(this)
                    .setGraph(R.navigation.navgraph_main)
                    .setDestination(R.id.playerFragment)
                    .setArguments(defaultArgsBundle)
                    .createPendingIntent()

                val notification = NotificationCompat.Builder(this, PODX_EVENT_CHANNEL)
                    .setContentText(textEvent.notification)
                    .setSmallIcon(R.drawable.ic_icons_article)
                    .setContentIntent(textPendingIntent)
                    .setGroup(PODX_TEXT_GROUP_KEY)
                    .build()

                notificationManager.notify(notificationId, notification)
            }
        }

        if (newAggregate) {
            val contentString = if (TextEventList.size > 1) {
                "${TextEventList.size} " +  getString(R.string.notification_text_content_plural)
            } else {
                "${TextEventList.size} " + getString(R.string.notification_text_content_singular)
            }
            val aggregateTextNotification = NotificationCompat.Builder(this, PODX_EVENT_CHANNEL)
                .setContentTitle(getString(R.string.notification_text_title))
                .setContentText(contentString)
                .setSmallIcon(R.drawable.ic_icons_article)
                .setGroup(PODX_TEXT_GROUP_KEY)
                .setGroupSummary(true)
                .build()

            notificationManager.notify(PODX_TEXT_AGGREGATE_ID, aggregateTextNotification)
        } else {
            if (TextEventList.isEmpty()) {
                notificationManager.cancel(PODX_TEXT_AGGREGATE_ID)
            }
        }
    }

    private val displayingWebIds = mutableListOf<Int>()
    private fun displayWebEvents(WebEventList: List<PodXWebEvent>) {
        val newAggregate = displayingWebIds.size == 0 && WebEventList.isNotEmpty()

        //remove notifications that aren't coming back from the emitter anymore
        displayingWebIds.filter {
            it !in WebEventList.map { web ->
                web.id % PODX_WEB_NOTIFICATION_RANGE +
                    PODX_WEB_NOTIFICATION_RANGE }
        }.forEach{
            notificationManager.cancel(it)
        }

        WebEventList.forEach { webEvent ->
            val notificationId = webEvent.id % PODX_WEB_NOTIFICATION_RANGE +
                PODX_WEB_NOTIFICATION_RANGE
            if (notificationId !in displayingWebIds) {
                displayingWebIds.add(notificationId)
                val webPendingIntent = NavDeepLinkBuilder(this)
                    .setGraph(R.navigation.navgraph_main)
                    .setDestination(R.id.playerFragment)
                    .setArguments(defaultArgsBundle)
                    .createPendingIntent()

                val notification = NotificationCompat.Builder(this, PODX_EVENT_CHANNEL)
                    .setContentText(webEvent.notification)
                    .setSmallIcon(R.drawable.ic_icons_social)
                    .setContentIntent(webPendingIntent)
                    .setGroup(PODX_WEB_GROUP_KEY)
                    .build()

                notificationManager.notify(notificationId, notification)
            }
        }

        if (newAggregate) {
            val contentString = if (WebEventList.size > 1) {
                "${WebEventList.size} " +  getString(R.string.notification_web_content_plural)
            } else {
                "${WebEventList.size} " + getString(R.string.notification_web_content_singular)
            }
            val aggregateWebNotification = NotificationCompat.Builder(this, PODX_EVENT_CHANNEL)
                .setContentTitle(getString(R.string.notification_web_title))
                .setContentText(contentString)
                .setSmallIcon(R.drawable.ic_icons_social)
                .setGroup(PODX_WEB_GROUP_KEY)
                .setGroupSummary(true)
                .build()

            notificationManager.notify(PODX_WEB_AGGREGATE_ID, aggregateWebNotification)
        } else {
            if (WebEventList.isEmpty()) {
                notificationManager.cancel(PODX_WEB_AGGREGATE_ID)
            }
        }
    }




    @RequiresApi(Build.VERSION_CODES.O)
    private fun eventChannelExists() =
        notificationManager.getNotificationChannel(PODX_EVENT_CHANNEL) != null

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createEventChannel() {
        val notificationChannel = NotificationChannel(
            PODX_EVENT_CHANNEL,
            getString(R.string.event_notification_channel),
            NotificationManager.IMPORTANCE_LOW)
            .apply {
                description = getString(R.string.event_notification_channel_description)
            }

        notificationManager.createNotificationChannel(notificationChannel)
    }

    companion object {
        const val PODX_EVENT_CHANNEL = "com.guardian.podx.events.PODX_EVENTS"
        const val PODX_IMAGE_GROUP_KEY = "com.guardian.podx.events.PODX_IMAGE"
        const val PODX_IMAGE_NOTIFICATION_RANGE = 0x10000
        const val PODX_IMAGE_AGGREGATE_ID = 0x1000
        const val PODX_CALL_PROMPT_GROUP_KEY = "com.guardian.podx.events.PODX_CALL_PROMPT"
        const val PODX_CALL_PROMPT_NOTIFICATION_RANGE = 0x20000
        const val PODX_CALL_PROMPT_AGGREGATE_ID = 0x2000
        const val PODX_FEED_BACK_GROUP_KEY = "com.guardian.podx.events.PODX_FEED_BACK"
        const val PODX_FEED_BACK_NOTIFICATION_RANGE = 0x30000
        const val PODX_FEED_BACK_AGGREGATE_ID = 0x3000
        const val PODX_FEED_LINK_GROUP_KEY = "com.guardian.podx.events.PODX_FEED_LINK"
        const val PODX_FEED_LINK_NOTIFICATION_RANGE = 0x40000
        const val PODX_FEED_LINK_AGGREGATE_ID = 0x4000
        const val PODX_NEWS_LETTER_SIGN_UP_GROUP_KEY = "com.guardian.podx.events.PODX_NEWS_LETTER_SIGN_UP"
        const val PODX_NEWS_LETTER_SIGN_UP_NOTIFICATION_RANGE = 0x50000
        const val PODX_NEWS_LETTER_SIGN_UP_AGGREGATE_ID = 0x5000
        const val PODX_POLL_GROUP_KEY = "com.guardian.podx.events.PODX_POLL"
        const val PODX_POLL_NOTIFICATION_RANGE = 0x60000
        const val PODX_POLL_AGGREGATE_ID = 0x6000
        const val PODX_SOCIAL_PROMPT_GROUP_KEY = "com.guardian.podx.events.PODX_SOCIAL_PROMPT"
        const val PODX_SOCIAL_PROMPT_NOTIFICATION_RANGE = 0x70000
        const val PODX_SOCIAL_PROMPT_AGGREGATE_ID = 0x7000
        const val PODX_SUPPORT_GROUP_KEY = "com.guardian.podx.events.PODX_SUPPORT"
        const val PODX_SUPPORT_NOTIFICATION_RANGE = 0x80000
        const val PODX_SUPPORT_AGGREGATE_ID = 0x8000
        const val PODX_TEXT_GROUP_KEY = "com.guardian.podx.events.PODX_TEXT"
        const val PODX_TEXT_NOTIFICATION_RANGE = 0x90000
        const val PODX_TEXT_AGGREGATE_ID = 0x9000
        const val PODX_WEB_GROUP_KEY = "com.guardian.podx.events.PODX_WEB"
        const val PODX_WEB_NOTIFICATION_RANGE = 0xA0000
        const val PODX_WEB_AGGREGATE_ID = 0xA000
    }
}