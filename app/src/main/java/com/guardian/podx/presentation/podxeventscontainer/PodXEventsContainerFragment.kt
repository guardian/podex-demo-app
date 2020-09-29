package com.guardian.podx.presentation.podxeventscontainer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.guardian.core.feeditem.FeedItem
import com.guardian.core.podxevent.PodXCallPromptEvent
import com.guardian.core.podxevent.PodXFeedBackEvent
import com.guardian.core.podxevent.PodXImageEvent
import com.guardian.core.podxevent.PodXNewsLetterSignUpEvent
import com.guardian.core.podxevent.PodXPollEvent
import com.guardian.core.podxevent.PodXSocialPromptEvent
import com.guardian.core.podxevent.PodXSupportEvent
import com.guardian.core.podxevent.PodXTextEvent
import com.guardian.core.podxevent.PodXWebEvent
import com.guardian.podx.R
import com.guardian.podx.databinding.LayoutPodxeventscontainerfragmentBinding
import com.guardian.podx.utils.lifecycleAwareVar
import timber.log.Timber
import java.util.concurrent.Executor
import javax.inject.Inject

class PodXEventsContainerFragment
@Inject constructor(
    viewModelProviderFactory: ViewModelProvider.Factory,
    private val executor: Executor
) :
    Fragment() {

    private val podXEventsContainerViewModel: PodXEventsContainerViewModel by viewModels {
        viewModelProviderFactory
    }

    private var binding: LayoutPodxeventscontainerfragmentBinding by lifecycleAwareVar()

    private val thumbnailMutableLiveData: MutableLiveData<List<PodXEventThumbnailData>> =
        MutableLiveData(listOf())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.layout_podxeventscontainerfragment,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindVisibilitySwitch()
        setupRecyclerView()
        feedThumbnailLiveData()
        setNewEventBehaviour()
    }

    // create unique ids for events so we can fire each once only
    private val lastList = mutableListOf<String>()

    private fun setNewEventBehaviour() {
        lastList.apply {
            addAll(
                podXEventsContainerViewModel
                    .podXEventsContainerUiModel
                    .podXImageEventsListLiveData
                    .value
                    ?.map { it.getThumbnailId() } ?: listOf()
            )

            addAll(
                podXEventsContainerViewModel
                    .podXEventsContainerUiModel
                    .podXCallPromptEventsListLiveData
                    .value
                    ?.map { it.getThumbnailId() } ?: listOf()
            )

            addAll(
                podXEventsContainerViewModel
                    .podXEventsContainerUiModel
                    .podXFeedBackEventsListLiveData
                    .value
                    ?.map { it.getThumbnailId() } ?: listOf()
            )

            addAll(
                podXEventsContainerViewModel
                    .podXEventsContainerUiModel
                    .podXFeedLinkEventsListLiveData
                    .value
                    ?.map { it.getThumbnailId() } ?: listOf()
            )

            addAll(
                podXEventsContainerViewModel
                    .podXEventsContainerUiModel
                    .podXNewsLetterSignUpEventsListLiveData
                    .value
                    ?.map { it.getThumbnailId() } ?: listOf()
            )

            addAll(
                podXEventsContainerViewModel
                    .podXEventsContainerUiModel
                    .podXPollEventsListLiveData
                    .value
                    ?.map { it.getThumbnailId() } ?: listOf()
            )

            addAll(
                podXEventsContainerViewModel
                    .podXEventsContainerUiModel
                    .podXSocialPromptEventsListLiveData
                    .value
                    ?.map { it.getThumbnailId() } ?: listOf()
            )

            addAll(
                podXEventsContainerViewModel
                    .podXEventsContainerUiModel
                    .podXSupportEventsListLiveData
                    .value
                    ?.map { it.getThumbnailId() } ?: listOf()
            )

            addAll(
                podXEventsContainerViewModel
                    .podXEventsContainerUiModel
                    .podXTextEventsListLiveData
                    .value
                    ?.map { it.getThumbnailId() } ?: listOf()
            )

            addAll(
                podXEventsContainerViewModel
                    .podXEventsContainerUiModel
                    .podXWebEventsListLiveData
                    .value
                    ?.map { it.getThumbnailId() } ?: listOf()
            )
        }

        podXEventsContainerViewModel
            .podXEventsContainerUiModel
            .podXCallPromptEventsListLiveData
            .observe(viewLifecycleOwner) { eventList ->
                if (eventList.isNotEmpty()) {
                    eventList.forEach {
                        if (!lastList.contains(it.getThumbnailId())) {
                            lastList.add(it.getThumbnailId())
                            navigateToCall(it, true)
                        }
                    }
                }

                podXEventsContainerViewModel
                    .podXEventsContainerUiModel
                    .podXFeedBackEventsListLiveData
                    .observe(viewLifecycleOwner) { eventList ->
                        if (eventList.isNotEmpty()) {
                            eventList.forEach {
                                if (!lastList.contains(it.getThumbnailId())) {
                                    lastList.add(it.getThumbnailId())
                                    navigateToFeedBackActivity(it, true)
                                }
                            }
                        }
                    }

                podXEventsContainerViewModel
                    .podXEventsContainerUiModel
                    .podXImageEventsListLiveData
                    .observe(viewLifecycleOwner) { eventList ->
                        if (eventList.isNotEmpty()) {
                            eventList.forEach {
                                if (!lastList.contains(it.getThumbnailId())) {
                                    lastList.add(it.getThumbnailId())
                                    Timber.i("navigate to id ${it.getThumbnailId()}")
                                    navigateToImage(it, true)
                                }
                            }
                        }
                    }

                podXEventsContainerViewModel
                    .podXEventsContainerUiModel
                    .podXNewsLetterSignUpEventsListLiveData
                    .observe(viewLifecycleOwner) { eventList ->
                        if (eventList.isNotEmpty()) {
                            eventList.forEach {
                                if (!lastList.contains(it.getThumbnailId())) {
                                    lastList.add(it.getThumbnailId())
                                    navigateToNewsLetterSignUpActivity(it, true)
                                }
                            }
                        }
                    }

                podXEventsContainerViewModel
                    .podXEventsContainerUiModel
                    .podXPollEventsListLiveData
                    .observe(viewLifecycleOwner) { eventList ->
                        if (eventList.isNotEmpty()) {
                            eventList.forEach {
                                if (!lastList.contains(it.getThumbnailId())) {
                                    lastList.add(it.getThumbnailId())
                                    navigateToPollActivity(it, true)
                                }
                            }
                        }
                    }

                podXEventsContainerViewModel
                    .podXEventsContainerUiModel
                    .podXSocialPromptEventsListLiveData
                    .observe(viewLifecycleOwner) { eventList ->
                        if (eventList.isNotEmpty()) {
                            eventList.forEach {
                                if (!lastList.contains(it.getThumbnailId())) {
                                    lastList.add(it.getThumbnailId())
                                    navigateToSocialPromptActivity(it, true)
                                }
                            }
                        }
                    }

                podXEventsContainerViewModel
                    .podXEventsContainerUiModel
                    .podXSupportEventsListLiveData
                    .observe(viewLifecycleOwner) { eventList ->
                        if (eventList.isNotEmpty()) {
                            eventList.forEach {
                                if (!lastList.contains(it.getThumbnailId())) {
                                    lastList.add(it.getThumbnailId())
                                    navigateToSupportActivity(it, true)
                                }
                            }
                        }
                    }

                podXEventsContainerViewModel
                    .podXEventsContainerUiModel
                    .podXTextEventsListLiveData
                    .observe(viewLifecycleOwner) { eventList ->
                        if (eventList.isNotEmpty()) {
                            eventList.forEach {
                                if (!lastList.contains(it.getThumbnailId())) {
                                    lastList.add(it.getThumbnailId())
                                    navigateToText(it, true)
                                }
                            }
                        }
                    }

                podXEventsContainerViewModel
                    .podXEventsContainerUiModel
                    .podXWebEventsListLiveData
                    .observe(viewLifecycleOwner) { eventList ->
                        if (eventList.isNotEmpty()) {
                            eventList.forEach {
                                if (!lastList.contains(it.getThumbnailId())) {
                                    lastList.add(it.getThumbnailId())
                                    navigateToWebActivity(it, true)
                                }
                            }
                        }
                    }
            }
    }

    private fun feedThumbnailLiveData() {
        val imageThumbnailData = podXEventsContainerViewModel
            .podXEventsContainerUiModel
            .episodePodXImageEventsListLiveData
            .map { imageList ->
                val resources = activity?.resources
                val theme = activity?.theme
                if (resources != null && theme != null) {
                    imageList.map { image ->
                        image.toPodXEventThumbnail(
                            onClickListener = {
                                navigateToImage(image, false)
                            },
                            resources = resources,
                            theme = theme
                        )
                    }
                } else {
                    listOf()
                }
            }

        val webThumbnailData = podXEventsContainerViewModel
            .podXEventsContainerUiModel
            .episodePodXWebEventsListLiveData
            .map { webList ->
                val resources = activity?.resources
                val theme = activity?.theme
                if (resources != null && theme != null) {
                    webList.map { web ->
                        web.toPodXEventThumbnail(
                            onClickListener = {
                                navigateToWebActivity(web, false)
                            },
                            resources = resources,
                            theme = theme
                        )
                    }
                } else {
                    listOf()
                }
            }

        val supportThumbnailData = podXEventsContainerViewModel
            .podXEventsContainerUiModel
            .episodePodXSupportEventsListLiveData
            .map { supportEvent ->
                val resources = activity?.resources
                val theme = activity?.theme
                if (resources != null && theme != null) {
                    supportEvent.map { support ->
                        support.toPodXEventThumbnail(
                            onClickListener = {
                                navigateToSupportActivity(support, false)
                            },
                            resources = resources,
                            theme = theme
                        )
                    }
                } else {
                    listOf()
                }
            }

        val textThumbnailData = podXEventsContainerViewModel
            .podXEventsContainerUiModel
            .episodePodXTextEventsListLiveData
            .map { textEvent ->
                val resources = activity?.resources
                val theme = activity?.theme
                if (resources != null && theme != null) {
                    textEvent.map { text ->
                        text.toPodXEventThumbnail(
                            onClickListener = {
                                navigateToText(text, false)
                            },
                            resources = resources,
                            theme = theme
                        )
                    }
                } else {
                    listOf()
                }
            }

        val callPromptThumbnailData = podXEventsContainerViewModel
            .podXEventsContainerUiModel
            .episodePodXCallPromptEventsListLiveData
            .map { callPromptEvent ->
                val resources = activity?.resources
                val theme = activity?.theme
                if (resources != null && theme != null) {
                    callPromptEvent.map { callPrompt ->
                        callPrompt.toPodXEventThumbnail(
                            onClickListener = {
                                navigateToCall(callPrompt, false)
                            },
                            resources = resources,
                            theme = theme
                        )
                    }
                } else {
                    listOf()
                }
            }

        val feedBackThumbnailData = podXEventsContainerViewModel
            .podXEventsContainerUiModel
            .episodePodXFeedBackEventsListLiveData
            .map { feedBackEvent ->
                val resources = activity?.resources
                val theme = activity?.theme
                if (resources != null && theme != null) {
                    feedBackEvent.map { feedBack ->
                        feedBack.toPodXEventThumbnail(
                            onClickListener = {
                                navigateToFeedBackActivity(feedBack, false)
                            },
                            resources = resources,
                            theme = theme
                        )
                    }
                } else {
                    listOf()
                }
            }

        val feedLinkThumbnailData = podXEventsContainerViewModel
            .podXEventsContainerUiModel
            .episodePodXFeedLinkEventsListLiveData
            .map { feedLinkEvent ->
                val resources = activity?.resources
                val theme = activity?.theme
                if (resources != null && theme != null) {
                    feedLinkEvent.map { feedLink ->
                        feedLink.toPodXEventThumbnail(
                            onClickListener = {
                                podXEventsContainerViewModel.openGetFeedItemFromFeedLink(feedLink)
                                    .subscribe(
                                        { feedItemFromLink ->
                                            navigateToFeedItem(feedItemFromLink)
                                        },
                                        { e -> Timber.e(e) }
                                    )
                            },
                            resources = resources,
                            theme = theme
                        )
                    }
                } else {
                    listOf()
                }
            }

        val newsLetterSignUpThumbnailData = podXEventsContainerViewModel
            .podXEventsContainerUiModel
            .episodePodXNewsLetterSignUpEventsListLiveData
            .map { newsLetterSignUpEvent ->
                val resources = activity?.resources
                val theme = activity?.theme
                if (resources != null && theme != null) {
                    newsLetterSignUpEvent.map { newsLetterSignUp ->
                        newsLetterSignUp.toPodXEventThumbnail(
                            onClickListener = {
                                navigateToNewsLetterSignUpActivity(newsLetterSignUp, false)
                            },
                            resources = resources,
                            theme = theme
                        )
                    }
                } else {
                    listOf()
                }
            }

        val pollThumbnailData = podXEventsContainerViewModel
            .podXEventsContainerUiModel
            .episodePodXPollEventsListLiveData
            .map { pollEvent ->
                val resources = activity?.resources
                val theme = activity?.theme
                if (resources != null && theme != null) {
                    pollEvent.map { poll ->
                        poll.toPodXEventThumbnail(
                            onClickListener = {
                                navigateToPollActivity(poll, false)
                            },
                            resources = resources,
                            theme = theme
                        )
                    }
                } else {
                    listOf()
                }
            }

        val socialPromptThumbnailData = podXEventsContainerViewModel
            .podXEventsContainerUiModel
            .episodePodXSocialPromptEventsListLiveData
            .map { socialPromptEvent ->
                val resources = activity?.resources
                val theme = activity?.theme
                if (resources != null && theme != null) {
                    socialPromptEvent.map { socialPrompt ->
                        socialPrompt.toPodXEventThumbnail(
                            onClickListener = {
                                navigateToSocialPromptActivity(socialPrompt, false)
                            },
                            resources = resources,
                            theme = theme
                        )
                    }
                } else {
                    listOf()
                }
            }

        MediatorLiveData<List<PodXEventThumbnailData>>()
            .apply {
                observe(viewLifecycleOwner) {
                    thumbnailMutableLiveData.postValue(it)
                }

                addSource(imageThumbnailData) {
                    postValue(
                        generateCurrentThumbnails(
                            imageThumbnailData, webThumbnailData,
                            supportThumbnailData, textThumbnailData, callPromptThumbnailData,
                            feedBackThumbnailData, feedLinkThumbnailData, newsLetterSignUpThumbnailData,
                            pollThumbnailData, socialPromptThumbnailData
                        )
                    )
                }

                addSource(webThumbnailData) {
                    postValue(
                        generateCurrentThumbnails(
                            imageThumbnailData, webThumbnailData,
                            supportThumbnailData, textThumbnailData, callPromptThumbnailData,
                            feedBackThumbnailData, feedLinkThumbnailData, newsLetterSignUpThumbnailData,
                            pollThumbnailData, socialPromptThumbnailData
                        )
                    )
                }

                addSource(supportThumbnailData) {
                    postValue(
                        generateCurrentThumbnails(
                            imageThumbnailData, webThumbnailData,
                            supportThumbnailData, textThumbnailData, callPromptThumbnailData,
                            feedBackThumbnailData, feedLinkThumbnailData, newsLetterSignUpThumbnailData,
                            pollThumbnailData, socialPromptThumbnailData
                        )
                    )
                }

                addSource(textThumbnailData) {
                    postValue(
                        generateCurrentThumbnails(
                            imageThumbnailData, webThumbnailData,
                            supportThumbnailData, textThumbnailData, callPromptThumbnailData,
                            feedBackThumbnailData, feedLinkThumbnailData, newsLetterSignUpThumbnailData,
                            pollThumbnailData, socialPromptThumbnailData
                        )
                    )
                }

                addSource(callPromptThumbnailData) {
                    postValue(
                        generateCurrentThumbnails(
                            imageThumbnailData, webThumbnailData,
                            supportThumbnailData, textThumbnailData, callPromptThumbnailData,
                            feedBackThumbnailData, feedLinkThumbnailData, newsLetterSignUpThumbnailData,
                            pollThumbnailData, socialPromptThumbnailData
                        )
                    )
                }

                addSource(feedBackThumbnailData) {
                    postValue(
                        generateCurrentThumbnails(
                            imageThumbnailData, webThumbnailData,
                            supportThumbnailData, textThumbnailData, callPromptThumbnailData,
                            feedBackThumbnailData, feedLinkThumbnailData, newsLetterSignUpThumbnailData,
                            pollThumbnailData, socialPromptThumbnailData
                        )
                    )
                }

                addSource(feedLinkThumbnailData) {
                    postValue(
                        generateCurrentThumbnails(
                            imageThumbnailData, webThumbnailData,
                            supportThumbnailData, textThumbnailData, callPromptThumbnailData,
                            feedBackThumbnailData, feedLinkThumbnailData, newsLetterSignUpThumbnailData,
                            pollThumbnailData, socialPromptThumbnailData
                        )
                    )
                }

                addSource(pollThumbnailData) {
                    postValue(
                        generateCurrentThumbnails(
                            imageThumbnailData, webThumbnailData,
                            supportThumbnailData, textThumbnailData, callPromptThumbnailData,
                            feedBackThumbnailData, feedLinkThumbnailData, newsLetterSignUpThumbnailData,
                            pollThumbnailData, socialPromptThumbnailData
                        )
                    )
                }

                addSource(socialPromptThumbnailData) {
                    postValue(
                        generateCurrentThumbnails(
                            imageThumbnailData, webThumbnailData,
                            supportThumbnailData, textThumbnailData, callPromptThumbnailData,
                            feedBackThumbnailData, feedLinkThumbnailData, newsLetterSignUpThumbnailData,
                            pollThumbnailData, socialPromptThumbnailData
                        )
                    )
                }
            }
    }

    private fun navigateToFeedItem(feedItemFromLink: FeedItem?) {
        if (feedItemFromLink != null) {
            podXEventsContainerViewModel.prepareFeedItemForPlayback(feedItemFromLink)

            findNavController()
                .navigate(R.id.action_global_playerFragment)
        }
    }

    private fun generateCurrentThumbnails(
        vararg eventsThumbnailLiveData: LiveData<List<PodXEventThumbnailData>>
    ): List<PodXEventThumbnailData> {
        val aggregateThumbnails = mutableListOf<PodXEventThumbnailData>()

        for (thumbnailLiveData in eventsThumbnailLiveData) {
            val thumbnailData = thumbnailLiveData.value
            if (!thumbnailData.isNullOrEmpty()) {
                aggregateThumbnails.addAll(thumbnailData)
            }
        }

        aggregateThumbnails.sortBy { it.timeStart }

        return aggregateThumbnails
    }

    private fun navigateToSupportActivity(podXSupportEvent: PodXSupportEvent, newEvent: Boolean) {
        val argsBundle = Bundle()
            .apply {
                putString("notification", podXSupportEvent.notification)
                putString("caption", podXSupportEvent.caption)
                putString("urlString", podXSupportEvent.urlString)
                putString("imageUrlString", podXSupportEvent.ogMetadata.OGImage)
                putLong("timeStart", podXSupportEvent.timeStart)
                putLong("timeEnd", podXSupportEvent.timeEnd)
                putInt("icon", R.drawable.ic_icons_link)
                putBoolean("newEventFlag", newEvent)
            }

        findNavController()
            .navigate(R.id.action_global_podXLinkFragment, argsBundle)
    }

    private fun navigateToWebActivity(podXWebEvent: PodXWebEvent, newEvent: Boolean) {
        val argsBundle = Bundle()
            .apply {
                putString("notification", podXWebEvent.notification)
                putString("caption", podXWebEvent.caption)
                putString("urlString", podXWebEvent.urlString)
                putString("imageUrlString", podXWebEvent.ogMetadata.OGImage)
                putLong("timeStart", podXWebEvent.timeStart)
                putLong("timeEnd", podXWebEvent.timeEnd)
                putInt("icon", R.drawable.ic_icons_link)
                putBoolean("newEventFlag", newEvent)
            }

        findNavController()
            .navigate(R.id.action_global_podXLinkFragment, argsBundle)
    }

    private fun navigateToFeedBackActivity(podXFeedBackEvent: PodXFeedBackEvent, newEvent: Boolean) {
        val argsBundle = Bundle()
            .apply {
                putString("notification", podXFeedBackEvent.notification)
                putString("caption", podXFeedBackEvent.caption)
                putString("urlString", podXFeedBackEvent.urlString)
                putString("imageUrlString", podXFeedBackEvent.ogMetadata.OGImage)
                putLong("timeStart", podXFeedBackEvent.timeStart)
                putLong("timeEnd", podXFeedBackEvent.timeEnd)
                putInt("icon", R.drawable.ic_icons_feedback)
                putBoolean("newEventFlag", newEvent)
            }

        findNavController()
            .navigate(R.id.action_global_podXLinkFragment, argsBundle)
    }

    private fun navigateToNewsLetterSignUpActivity(podXNewsLetterSignUpEvent: PodXNewsLetterSignUpEvent, newEvent: Boolean) {
        val argsBundle = Bundle()
            .apply {
                putString("notification", podXNewsLetterSignUpEvent.notification)
                putString("caption", podXNewsLetterSignUpEvent.caption)
                putString("urlString", podXNewsLetterSignUpEvent.urlString)
                putString("imageUrlString", podXNewsLetterSignUpEvent.ogMetadata.OGImage)
                putLong("timeStart", podXNewsLetterSignUpEvent.timeStart)
                putLong("timeEnd", podXNewsLetterSignUpEvent.timeEnd)
                putInt("icon", R.drawable.ic_icons_newsletter)
                putBoolean("newEventFlag", newEvent)
            }

        findNavController()
            .navigate(R.id.action_global_podXLinkFragment, argsBundle)
    }

    private fun navigateToPollActivity(podXPollEvent: PodXPollEvent, newEvent: Boolean) {
        val argsBundle = Bundle()
            .apply {
                putString("notification", podXPollEvent.notification)
                putString("caption", podXPollEvent.caption)
                putString("urlString", podXPollEvent.urlString)
                putString("imageUrlString", podXPollEvent.ogMetadata.OGImage)
                putLong("timeStart", podXPollEvent.timeStart)
                putLong("timeEnd", podXPollEvent.timeEnd)
                putInt("icon", R.drawable.ic_icons_poll)
                putBoolean("newEventFlag", newEvent)
            }

        findNavController()
            .navigate(R.id.action_global_podXLinkFragment, argsBundle)
    }

    private fun navigateToSocialPromptActivity(podXSocialPromptEvent: PodXSocialPromptEvent, newEvent: Boolean) {
        val argsBundle = Bundle()
            .apply {
                putString("notification", podXSocialPromptEvent.notification)
                putString("caption", podXSocialPromptEvent.caption)
                putString("urlString", podXSocialPromptEvent.socialLinkUrlString)
                putString("imageUrlString", podXSocialPromptEvent.ogMetadata.OGImage)
                putLong("timeStart", podXSocialPromptEvent.timeStart)
                putLong("timeEnd", podXSocialPromptEvent.timeEnd)
                putInt("icon", R.drawable.ic_icons_social)
                putBoolean("newEventFlag", newEvent)
            }

        findNavController()
            .navigate(R.id.action_global_podXLinkFragment, argsBundle)
    }

    private fun navigateToImage(podXImageEvent: PodXImageEvent, newEvent: Boolean) {
        val argsBundle = Bundle()
            .apply {
                putParcelable("podXImageEvent", podXImageEvent)
                putBoolean("newEventFlag", newEvent)
            }

        findNavController()
            .navigate(R.id.action_global_podXImageFragment, argsBundle)
    }

    private fun navigateToText(podXTextEvent: PodXTextEvent, newEvent: Boolean) {
        val argsBundle = Bundle()
            .apply {
                putParcelable("podXTextEvent", podXTextEvent)
                putBoolean("newEventFlag", newEvent)
            }

        findNavController()
            .navigate(R.id.action_global_podXTextFragment, argsBundle)
    }

    private fun navigateToCall(podXCallPromptEvent: PodXCallPromptEvent, newEvent: Boolean) {
        // val argsBundle = Bundle()
        //     .apply {
        //         putParcelable("podXCallEvent", podXCallPromptEvent)
        //         putBoolean("newEventFlag", newEvent)
        //     }
        //
        // findNavController()
        //     .navigate(R.id.action_global_podXCallPromptFragment, argsBundle)
    }

    private fun setupRecyclerView() {
        binding
            .recyclerviewPodxeventscontainerEvents
            .layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.VERTICAL, false
        )

        binding
            .recyclerviewPodxeventscontainerEvents
            .adapter = PodXEventListAdapter(
            callback = object : DiffUtil.ItemCallback<PodXEventThumbnailData>() {
                override fun areItemsTheSame(oldItem: PodXEventThumbnailData, newItem: PodXEventThumbnailData): Boolean =
                    oldItem == newItem

                override fun areContentsTheSame(oldItem: PodXEventThumbnailData, newItem: PodXEventThumbnailData): Boolean =
                    oldItem.captionString == newItem.captionString &&
                        oldItem.imageUrlString == newItem.imageUrlString
            },
            executor = executor,
            navigateToTimestampMethod = { timeStamp ->
                podXEventsContainerViewModel.skipToTimestamp(timeStamp)
            }
        )
            .apply {
                thumbnailMutableLiveData
                    .observe(
                        viewLifecycleOwner,
                        Observer {
                            submitList(it)
                        }
                    )
            }
    }

    private fun bindVisibilitySwitch() {
        thumbnailMutableLiveData
            .observe(
                viewLifecycleOwner,
                Observer {
                    binding
                        .constraintlayoutPodxeventscontainerRoot
                        .visibility = if (it.isEmpty()) {
                        View.GONE
                    } else {
                        View.VISIBLE
                    }
                }
            )
    }

    companion object {
        private const val PHONE_POPUP_TAG = "phonePopup"
    }
}
