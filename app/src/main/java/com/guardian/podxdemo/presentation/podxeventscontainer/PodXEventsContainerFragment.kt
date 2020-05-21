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
import com.guardian.core.podxevent.PodXImageEvent
import com.guardian.core.podxevent.PodXSupportEvent
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

        val imageThumbnailData = MutableLiveData<List<PodXEventThumbnailData>> ()
        podXEventsContainerViewModel
            .podXEventsContainerUiModel
            .podXImageEventsListLiveData
            .observe(viewLifecycleOwner) { imageList ->
                imageThumbnailData.postValue(imageList.map { image ->
                    image.toPodXEventThumbnail(
                        onClickListener = View.OnClickListener {
                            navigateToImage(image)
                        }
                    )
                })
            }

        val webThumbnailData = podXEventsContainerViewModel
            .podXEventsContainerUiModel
            .podXWebEventsListLiveData
            .map { webList ->
                webList.map { web ->
                    web.toPodXEventThumbnail(
                        onClickListener = View.OnClickListener {
                            navigateToWeb(web)
                        }
                    )
                }
            }

        val supportThumbailData = podXEventsContainerViewModel
            .podXEventsContainerUiModel
            .podXSupportEventsListLiveData
            .map { supportEvent ->
                supportEvent.map { support ->
                    support.toPodXEventThumbnail(
                        onClickListener = View.OnClickListener {
                            navigateToSupport(support)
                        }
                    )
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
                feedBackEvent.map { feedBack ->
                    feedBack.toPodXEventThumbnail(
                        onClickListener = View.OnClickListener {
                        }
                    )
                }
            }

        val feedLinkThumbnailData = podXEventsContainerViewModel
            .podXEventsContainerUiModel
            .podXFeedLinkEventsListLiveData
            .map { feedLinkEvent ->
                feedLinkEvent.map { feedLink ->
                    feedLink.toPodXEventThumbnail(
                        onClickListener = View.OnClickListener {
                            podXEventsContainerViewModel.openGetFeedItemFromFeedLink(feedLink)
                                .subscribe({feedItemFromLink ->
                                    Timber.i("attempting to navigate to feel link ${feedItemFromLink.feedItemAudioUrl}")
                                    navigateToFeedItem(feedItemFromLink)
                                },
                                    {e -> Timber.e(e)}
                                )
                        }
                    )
                }
            }

        val newsLetterSignUpThumbnailData = podXEventsContainerViewModel
            .podXEventsContainerUiModel
            .podXNewsLetterSignUpEventsListLiveData
            .map { newsLetterSignUpEvent ->
                newsLetterSignUpEvent.map { newsLetterSignUp ->
                    newsLetterSignUp.toPodXEventThumbnail(
                        onClickListener = View.OnClickListener {
                        }
                    )
                }
            }

        val pollThumbnailData = podXEventsContainerViewModel
            .podXEventsContainerUiModel
            .podXPollEventsListLiveData
            .map { pollEvent ->
                pollEvent.map { poll ->
                    poll.toPodXEventThumbnail(
                        onClickListener = View.OnClickListener {
                        }
                    )
                }
            }

        val socialPromptThumbnailData = podXEventsContainerViewModel
            .podXEventsContainerUiModel
            .podXSocialPromptEventsListLiveData
            .map { socialPromptEvent ->
                socialPromptEvent.map { socialPrompt ->
                    socialPrompt.toPodXEventThumbnail(
                        onClickListener = View.OnClickListener {
                        }
                    )
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
            val argsBundle = Bundle()
                .apply {
                    putParcelable("podXSupportEvent", podXSupportEvent)
                }

            findNavController()
                .navigate(R.id.action_global_playerFragment, argsBundle)
        }
    }

    private fun navigateToWeb(podXWebEvent: PodXWebEvent) {
        if (podXWebEvent.urlString.isNotBlank()) {
            val webPage: Uri = Uri.parse(podXWebEvent.urlString)
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