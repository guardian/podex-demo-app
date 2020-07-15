package com.guardian.podxdemo.presentation.podxeventscontainer

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
import com.guardian.podxdemo.R
import com.guardian.podxdemo.databinding.LayoutPodxeventscontainerfragmentBinding
import com.guardian.podxdemo.utils.lifecycleAwareVar
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
        feedThumnailLiveData()
    }

    private fun feedThumnailLiveData() {

        val imageThumbnailData = podXEventsContainerViewModel
            .podXEventsContainerUiModel
            .podXImageEventsListLiveData
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
            .podXWebEventsListLiveData
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

        val supportThumbailData = podXEventsContainerViewModel
            .podXEventsContainerUiModel
            .podXSupportEventsListLiveData
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
            .podXTextEventsListLiveData
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
            .podXCallPromptEventsListLiveData
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
            .podXFeedBackEventsListLiveData
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
            .podXFeedLinkEventsListLiveData
            .map { feedLinkEvent ->
                val resources = activity?.resources
                val theme = activity?.theme
                if (resources != null && theme != null) {
                    feedLinkEvent.map { feedLink ->
                        feedLink.toPodXEventThumbnail(
                            onClickListener = View.OnClickListener {
                                podXEventsContainerViewModel.openGetFeedItemFromFeedLink(feedLink)
                                    .subscribe({ feedItemFromLink ->
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
            .podXNewsLetterSignUpEventsListLiveData
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
            .podXPollEventsListLiveData
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
            .podXSocialPromptEventsListLiveData
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
                    postValue(generatecurrentThumbnails(imageThumbnailData, webThumbnailData,
                        supportThumbailData, textThumbnailData, callPromptThumbnailData,
                        feedBackThumbnailData, feedLinkThumbnailData, newsLetterSignUpThumbnailData,
                        pollThumbnailData, socialPromptThumbnailData))
                }

                 addSource(webThumbnailData) {
                     postValue(generatecurrentThumbnails(imageThumbnailData, webThumbnailData,
                         supportThumbailData, textThumbnailData, callPromptThumbnailData,
                         feedBackThumbnailData, feedLinkThumbnailData, newsLetterSignUpThumbnailData,
                         pollThumbnailData, socialPromptThumbnailData))
                 }

                addSource(supportThumbailData) {
                    postValue(generatecurrentThumbnails(imageThumbnailData, webThumbnailData,
                        supportThumbailData, textThumbnailData, callPromptThumbnailData,
                        feedBackThumbnailData, feedLinkThumbnailData, newsLetterSignUpThumbnailData,
                        pollThumbnailData, socialPromptThumbnailData))
                }

                addSource(textThumbnailData) {
                    postValue(generatecurrentThumbnails(imageThumbnailData, webThumbnailData,
                        supportThumbailData, textThumbnailData, callPromptThumbnailData,
                        feedBackThumbnailData, feedLinkThumbnailData, newsLetterSignUpThumbnailData,
                        pollThumbnailData, socialPromptThumbnailData))
                }

                addSource(callPromptThumbnailData) {
                    postValue(generatecurrentThumbnails(imageThumbnailData, webThumbnailData,
                        supportThumbailData, textThumbnailData, callPromptThumbnailData,
                        feedBackThumbnailData, feedLinkThumbnailData, newsLetterSignUpThumbnailData,
                        pollThumbnailData, socialPromptThumbnailData))
                }

                addSource(feedBackThumbnailData) {
                    postValue(generatecurrentThumbnails(imageThumbnailData, webThumbnailData,
                        supportThumbailData, textThumbnailData, callPromptThumbnailData,
                        feedBackThumbnailData, feedLinkThumbnailData, newsLetterSignUpThumbnailData,
                        pollThumbnailData, socialPromptThumbnailData))
                }

                addSource(feedLinkThumbnailData) {
                    postValue(generatecurrentThumbnails(imageThumbnailData, webThumbnailData,
                        supportThumbailData, textThumbnailData, callPromptThumbnailData,
                        feedBackThumbnailData, feedLinkThumbnailData, newsLetterSignUpThumbnailData,
                        pollThumbnailData, socialPromptThumbnailData))
                }

                addSource(pollThumbnailData) {
                    postValue(generatecurrentThumbnails(imageThumbnailData, webThumbnailData,
                        supportThumbailData, textThumbnailData, callPromptThumbnailData,
                        feedBackThumbnailData, feedLinkThumbnailData, newsLetterSignUpThumbnailData,
                        pollThumbnailData, socialPromptThumbnailData))
                }

                addSource(socialPromptThumbnailData) {
                    postValue(generatecurrentThumbnails(imageThumbnailData, webThumbnailData,
                        supportThumbailData, textThumbnailData, callPromptThumbnailData,
                        feedBackThumbnailData, feedLinkThumbnailData, newsLetterSignUpThumbnailData,
                        pollThumbnailData, socialPromptThumbnailData))
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

    private fun generatecurrentThumbnails(
        vararg eventsThumbnailLiveDatas: LiveData<List<PodXEventThumbnailData>>
    ) : List<PodXEventThumbnailData> {
        val aggregateThumbnails = mutableListOf<PodXEventThumbnailData>()

        for (thumbnailLiveData in eventsThumbnailLiveDatas) {
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

    private fun navigateToWeb(podXWebEvent: PodXWebEvent) {
        if (podXWebEvent.urlString.isNotBlank()) {
            val webPage: Uri = Uri.parse(podXWebEvent.urlString)
            val intent = Intent(Intent.ACTION_VIEW, webPage)
            startActivity(intent)
        }
    }

    private fun navigateToFeedBack(podXFeedBackEvent: PodXFeedBackEvent) {
        if (podXFeedBackEvent.urlString.isNotBlank()) {
            val webPage: Uri = Uri.parse(podXFeedBackEvent.urlString)
            val intent = Intent(Intent.ACTION_VIEW, webPage)
            startActivity(intent)
        }
    }

    private fun navigateToNewsLetterSignUp(podXNewsLetterSignUpEvent: PodXNewsLetterSignUpEvent) {
        if (podXNewsLetterSignUpEvent.urlString.isNotBlank()) {
            val webPage: Uri = Uri.parse(podXNewsLetterSignUpEvent.urlString)
            val intent = Intent(Intent.ACTION_VIEW, webPage)
            startActivity(intent)
        }
    }

    private fun navigateToPoll (podXPollEvent: PodXPollEvent) {
        if (podXPollEvent.urlString.isNotBlank()) {
            val webPage: Uri = Uri.parse(podXPollEvent.urlString)
            val intent = Intent(Intent.ACTION_VIEW, webPage)
            startActivity(intent)
        }
    }

    private fun navigateToSocialPrompt(podXSocialPromptEvent: PodXSocialPromptEvent) {
        if (podXSocialPromptEvent.socialLinkUrlString.isNotBlank()) {
            val webPage: Uri = Uri.parse(podXSocialPromptEvent.socialLinkUrlString)
            val intent = Intent(Intent.ACTION_VIEW, webPage)
            startActivity(intent)
        }
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
                .show(fragmentManager, "not sure bout dis")
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
                    .observe(viewLifecycleOwner,
                        Observer {
                            submitList(it)
                        })
            }
    }

    private fun bindVisibilitySwitch() {
        thumbnailMutableLiveData
            .observe(viewLifecycleOwner,
                Observer {
                    binding
                        .constraintlayoutPodxeventscontainerRoot
                        .visibility = if (it.isEmpty()) {
                        View.GONE
                    } else {
                        View.VISIBLE
                    }
                })
    }
}