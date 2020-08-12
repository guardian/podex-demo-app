package com.guardian.podx.presentation.podxeventscontainer

import android.content.Intent
import android.net.Uri
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
    //
    // import com.guardian.core.podxevent.PodXCallPromptEvent
    // import com.guardian.core.podxevent.PodXFeedBackEvent
    // import com.guardian.core.podxevent.PodXImageEvent
    // import com.guardian.core.podxevent.PodXNewsLetterSignUpEvent
    // import com.guardian.core.podxevent.PodXPollEvent
    // import com.guardian.core.podxevent.PodXSocialPromptEvent
    // import com.guardian.core.podxevent.PodXSupportEvent
    // import com.guardian.core.podxevent.PodXTextEvent
    // import com.guardian.core.podxevent.PodXWebEvent
    //create unique ids for events so we can fire each once only
    private val lastList = mutableListOf<String>()
    private fun setNewEventBehaviour() {
        val suppressInit = lastList.isEmpty() && podXEventsContainerViewModel
            .podXEventsContainerUiModel
            .hasEvents
        //setNewCallPromptBehaviour(suppressInit)
        setNewFeedBackBehaviour(suppressInit)
        setNewImageBehaviour(suppressInit)
        setNewNewsLetterSignUpBehaviour(suppressInit)
        setNewPollBehaviour(suppressInit)
        setNewSocialPromptBehaviour(suppressInit)
        setNewSupportBehaviour(suppressInit)
        setNewTextBehaviour(suppressInit)
        setNewWebBehaviour(suppressInit)
    }

    // private fun setNewCallPromptBehaviour(suppressInit: Boolean) {
    //     var suppressCallPrompt = suppressInit
    //     podXEventsContainerViewModel
    //         .podXEventsContainerUiModel
    //         .podXCallPromptEventsListLiveData
    //         .observe(viewLifecycleOwner) { callPromptEvents ->
    //             val callPromptUnit = callPromptEvents.filter { "callPrompt${it.id}" !in lastList }
    //                 .map{callPromptEvent ->
    //                     {(callPromptEvent)}
    //                 }.firstOrNull()
    //
    //             lastList.addAll(callPromptEvents.map { "callPrompt${it.id}" })
    //
    //             if (!suppressCallPrompt && callPromptUnit != null) {
    //                 callPromptUnit()
    //             }
    //             suppressCallPrompt = false
    //         }
    // }

    private fun setNewFeedBackBehaviour(suppressInit: Boolean) {
        var suppressFeedBack = suppressInit
        podXEventsContainerViewModel
            .podXEventsContainerUiModel
            .podXFeedBackEventsListLiveData
            .observe(viewLifecycleOwner) { feedBackEvents ->
                val feedBackUnit = feedBackEvents.filter { "feedBack${it.id}" !in lastList }
                    .map{feedBackEvent ->
                        {navigateToFeedBackActivity(feedBackEvent)}
                    }.firstOrNull()

                lastList.addAll(feedBackEvents.map { "feedBack${it.id}" })

                if (!suppressFeedBack && feedBackUnit != null) {
                    feedBackUnit()
                }
                suppressFeedBack = false
            }
    }


    private fun setNewImageBehaviour(suppressInit: Boolean) {
        var suppressImage = suppressInit
        podXEventsContainerViewModel
            .podXEventsContainerUiModel
            .podXImageEventsListLiveData
            .observe(viewLifecycleOwner) { imageEvents ->
                val imageUnit = imageEvents.filter { "image${it.id}" !in lastList }
                    .map{imageEvent ->
                        {navigateToImage(imageEvent)}
                    }.firstOrNull()

                lastList.addAll(imageEvents.map { "image${it.id}" })

                if (!suppressImage && imageUnit != null) {
                    imageUnit()
                }
                suppressImage = false
            }
    }

    private fun setNewNewsLetterSignUpBehaviour(suppressInit: Boolean) {
        var suppressNewsLetterSignUp = suppressInit
        podXEventsContainerViewModel
            .podXEventsContainerUiModel
            .podXNewsLetterSignUpEventsListLiveData
            .observe(viewLifecycleOwner) { newsLetterSignUpEvents ->
                val newsLetterSignUpUnit = newsLetterSignUpEvents.filter { "newsLetterSignUp${it.id}" !in lastList }
                    .map{newsLetterSignUpEvent ->
                        {navigateToNewsLetterSignUpActivity(newsLetterSignUpEvent)}
                    }.firstOrNull()

                lastList.addAll(newsLetterSignUpEvents.map { "newsLetterSignUp${it.id}" })

                if (!suppressNewsLetterSignUp && newsLetterSignUpUnit != null) {
                    newsLetterSignUpUnit()
                }
                suppressNewsLetterSignUp = false
            }
    }

    private fun setNewPollBehaviour(suppressInit: Boolean) {
        var suppressPoll = suppressInit
        podXEventsContainerViewModel
            .podXEventsContainerUiModel
            .podXPollEventsListLiveData
            .observe(viewLifecycleOwner) { pollEvents ->
                val pollUnit = pollEvents.filter { "poll${it.id}" !in lastList }
                    .map{pollEvent ->
                        {navigateToPollActivity(pollEvent)}
                    }.firstOrNull()

                lastList.addAll(pollEvents.map { "poll${it.id}" })

                if (!suppressPoll && pollUnit != null) {
                    pollUnit()
                }
                suppressPoll = false
            }
    }

    private fun setNewSocialPromptBehaviour(suppressInit: Boolean) {
        var suppressSocialPrompt = suppressInit
        podXEventsContainerViewModel
            .podXEventsContainerUiModel
            .podXSocialPromptEventsListLiveData
            .observe(viewLifecycleOwner) { socialPromptEvents ->
                val socialPromptUnit = socialPromptEvents.filter { "socialPrompt${it.id}" !in lastList }
                    .map{socialPromptEvent ->
                        {navigateToSocialPromptActivity(socialPromptEvent)}
                    }.firstOrNull()

                lastList.addAll(socialPromptEvents.map { "socialPrompt${it.id}" })

                if (!suppressSocialPrompt && socialPromptUnit != null) {
                    socialPromptUnit()
                }
                suppressSocialPrompt = false
            }
    }

    private fun setNewSupportBehaviour(suppressInit: Boolean) {
        var suppressSupport = suppressInit
        podXEventsContainerViewModel
            .podXEventsContainerUiModel
            .podXSupportEventsListLiveData
            .observe(viewLifecycleOwner) { supportEvents ->
                val supportUnit = supportEvents.filter { "support${it.id}" !in lastList }
                    .map{supportEvent ->
                        {navigateToSupportActivity(supportEvent)}
                    }.firstOrNull()

                lastList.addAll(supportEvents.map { "support${it.id}" })

                if (!suppressSupport && supportUnit != null) {
                    supportUnit()
                }
                suppressSupport = false
            }
    }

    private fun setNewTextBehaviour(suppressInit: Boolean) {
        var suppressText = suppressInit
        podXEventsContainerViewModel
            .podXEventsContainerUiModel
            .podXTextEventsListLiveData
            .observe(viewLifecycleOwner) { textEvents ->
                val textUnit = textEvents.filter { "text${it.id}" !in lastList }
                    .map{textEvent ->
                        {navigateToText(textEvent)}
                    }.firstOrNull()

                lastList.addAll(textEvents.map { "text${it.id}" })

                if (!suppressText && textUnit != null) {
                    textUnit()
                }
                suppressText = false
            }
    }

    private fun setNewWebBehaviour(suppressInit: Boolean) {
        var suppressWeb = suppressInit
        podXEventsContainerViewModel
            .podXEventsContainerUiModel
            .podXWebEventsListLiveData
            .observe(viewLifecycleOwner) { webEvents ->
                val webUnit = webEvents.filter { "web${it.id}" !in lastList }
                    .map{webEvent ->
                        {navigateToWeb(webEvent)}
                    }.firstOrNull()

                lastList.addAll(webEvents.map { "web${it.id}" })

                if (!suppressWeb && webUnit != null) {
                    webUnit()
                }
                suppressWeb = false
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
                            onClickListener = View.OnClickListener {
                                navigateToImage(image)
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
                            onClickListener = View.OnClickListener {
                                navigateToWeb(web)
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
                            onClickListener = View.OnClickListener {
                                navigateToSupport(support)
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
                            onClickListener = View.OnClickListener {
                                navigateToText(text)
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
                            onClickListener = View.OnClickListener {
                                navigateToCall(callPrompt)
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
                            onClickListener = View.OnClickListener {
                                navigateToFeedBack(feedBack)
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
                            onClickListener = View.OnClickListener {
                                podXEventsContainerViewModel.openGetFeedItemFromFeedLink(feedLink)
                                    .subscribe(
                                        { feedItemFromLink ->
                                            Timber.i("attempting to navigate to feel link ${feedItemFromLink.feedItemAudioUrl}")
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
                            onClickListener = View.OnClickListener {
                                navigateToNewsLetterSignUp(newsLetterSignUp)
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
                            onClickListener = View.OnClickListener {
                                navigateToPoll(poll)
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
                            onClickListener = View.OnClickListener {
                                navigateToSocialPrompt(socialPrompt)
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
        Timber.i("got to navigate function with ${feedItemFromLink?.feedItemAudioUrl}")
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

        return aggregateThumbnails
    }

    private fun navigateToSupport(podXSupportEvent: PodXSupportEvent) {
        if (podXSupportEvent.urlString.isNotBlank()) {
            val webPage: Uri = Uri.parse(podXSupportEvent.urlString)
            val intent = Intent(Intent.ACTION_VIEW, webPage)
            startActivity(intent)
        }
    }

    private fun navigateToSupportActivity(podXSupportEvent: PodXSupportEvent) {
        val argsBundle = Bundle()
            .apply {
                putString("notification", podXSupportEvent.notification)
                putString("caption", podXSupportEvent.caption)
                putString("urlString", podXSupportEvent.urlString)
                putString("imageUrlString", podXSupportEvent.ogMetadata.OGImage)
                putInt("icon", R.drawable.ic_icons_link)
            }

        findNavController()
            .navigate(R.id.action_global_podXLinkFragment, argsBundle)
    }

    private fun navigateToWeb(podXWebEvent: PodXWebEvent) {
        if (podXWebEvent.urlString.isNotBlank()) {
            val webPage: Uri = Uri.parse(podXWebEvent.urlString)
            val intent = Intent(Intent.ACTION_VIEW, webPage)
            startActivity(intent)
        }
    }

    private fun navigateToWebActivity(podXWebEvent: PodXWebEvent) {
        val argsBundle = Bundle()
            .apply {
                putString("notification", podXWebEvent.notification)
                putString("caption", podXWebEvent.caption)
                putString("urlString", podXWebEvent.urlString)
                putString("imageUrlString", podXWebEvent.ogMetadata.OGImage)
                putInt("icon", R.drawable.ic_icons_link)
            }

        findNavController()
            .navigate(R.id.action_global_podXLinkFragment, argsBundle)
    }

    private fun navigateToFeedBack(podXFeedBackEvent: PodXFeedBackEvent) {
        if (podXFeedBackEvent.urlString.isNotBlank()) {
            val webPage: Uri = Uri.parse(podXFeedBackEvent.urlString)
            val intent = Intent(Intent.ACTION_VIEW, webPage)
            startActivity(intent)
        }
    }

    private fun navigateToFeedBackActivity(podXFeedBackEvent: PodXFeedBackEvent) {
        val argsBundle = Bundle()
            .apply {
                putString("notification", podXFeedBackEvent.notification)
                putString("caption", podXFeedBackEvent.caption)
                putString("urlString", podXFeedBackEvent.urlString)
                putString("imageUrlString", podXFeedBackEvent.ogMetadata.OGImage)
                putInt("icon", R.drawable.ic_icons_feedback)
            }

        findNavController()
            .navigate(R.id.action_global_podXLinkFragment, argsBundle)
    }

    private fun navigateToNewsLetterSignUp(podXNewsLetterSignUpEvent: PodXNewsLetterSignUpEvent) {
        if (podXNewsLetterSignUpEvent.urlString.isNotBlank()) {
            val webPage: Uri = Uri.parse(podXNewsLetterSignUpEvent.urlString)
            val intent = Intent(Intent.ACTION_VIEW, webPage)
            startActivity(intent)
        }
    }

    private fun navigateToNewsLetterSignUpActivity(podXNewsLetterSignUpEvent: PodXNewsLetterSignUpEvent) {
        val argsBundle = Bundle()
            .apply {
                putString("notification", podXNewsLetterSignUpEvent.notification)
                putString("caption", podXNewsLetterSignUpEvent.caption)
                putString("urlString", podXNewsLetterSignUpEvent.urlString)
                putString("imageUrlString", podXNewsLetterSignUpEvent.ogMetadata.OGImage)
                putInt("icon", R.drawable.ic_icons_newsletter)
            }

        findNavController()
            .navigate(R.id.action_global_podXLinkFragment, argsBundle)
    }

    private fun navigateToPoll(podXPollEvent: PodXPollEvent) {
        if (podXPollEvent.urlString.isNotBlank()) {
            val webPage: Uri = Uri.parse(podXPollEvent.urlString)
            val intent = Intent(Intent.ACTION_VIEW, webPage)
            startActivity(intent)
        }
    }

    private fun navigateToPollActivity(podXPollEvent: PodXPollEvent) {
        val argsBundle = Bundle()
            .apply {
                putString("notification", podXPollEvent.notification)
                putString("caption", podXPollEvent.caption)
                putString("urlString", podXPollEvent.urlString)
                putString("imageUrlString", podXPollEvent.ogMetadata.OGImage)
                putInt("icon", R.drawable.ic_icons_poll)
            }

        findNavController()
            .navigate(R.id.action_global_podXLinkFragment, argsBundle)
    }

    private fun navigateToSocialPrompt(podXSocialPromptEvent: PodXSocialPromptEvent) {
        if (podXSocialPromptEvent.socialLinkUrlString.isNotBlank()) {
            val webPage: Uri = Uri.parse(podXSocialPromptEvent.socialLinkUrlString)
            val intent = Intent(Intent.ACTION_VIEW, webPage)
            startActivity(intent)
        }
    }

    private fun navigateToSocialPromptActivity(podXSocialPromptEvent: PodXSocialPromptEvent) {
        val argsBundle = Bundle()
            .apply {
                putString("notification", podXSocialPromptEvent.notification)
                putString("caption", podXSocialPromptEvent.caption)
                putString("urlString", podXSocialPromptEvent.socialLinkUrlString)
                putString("imageUrlString", podXSocialPromptEvent.ogMetadata.OGImage)
                putInt("icon", R.drawable.ic_icons_social)
            }

        findNavController()
            .navigate(R.id.action_global_podXLinkFragment, argsBundle)
    }

    private fun navigateToImage(podXImageEvent: PodXImageEvent) {
        val argsBundle = Bundle()
            .apply {
                putParcelable("podXImageEvent", podXImageEvent)
            }

        findNavController()
            .navigate(R.id.action_global_podXImageFragment, argsBundle)
    }

    private fun navigateToText(podXTextEvent: PodXTextEvent) {
        val argsBundle = Bundle()
            .apply {
                putParcelable("podXTextEvent", podXTextEvent)
            }

        findNavController()
            .navigate(R.id.action_global_podXTextFragment, argsBundle)
    }

    private fun navigateToCall(podXCallPromptEvent: PodXCallPromptEvent) {
        val fragmentManager = activity?.supportFragmentManager
        if (fragmentManager != null) {
            PodXCallDialogFragment(podXCallPromptEvent.phoneNumber)
                .show(fragmentManager, PHONE_POPUP_TAG)
        }
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
            executor = executor
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
