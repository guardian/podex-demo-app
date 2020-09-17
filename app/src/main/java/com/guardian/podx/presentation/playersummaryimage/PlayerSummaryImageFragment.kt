package com.guardian.podx.presentation.playersummaryimage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
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
import com.guardian.podx.databinding.LayoutPlayersummaryimagefragmentBinding
import com.guardian.podx.presentation.podxeventscontainer.PodXCallDialogFragment
import com.guardian.podx.presentation.podxeventscontainer.PodXEventThumbnailData
import com.guardian.podx.utils.lifecycleAwareVar
import timber.log.Timber
import javax.inject.Inject

class PlayerSummaryImageFragment
@Inject constructor(viewModelProviderFactory: ViewModelProvider.Factory) :
    Fragment() {

    private val playerSummaryImageViewModel: PlayerSummaryImageViewModel by viewModels {
        viewModelProviderFactory
    }

    private var binding: LayoutPlayersummaryimagefragmentBinding by lifecycleAwareVar()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.layout_playersummaryimagefragment,
            container,
            false
        )

        initialisePreview()
        setupPreview()

        return binding.root
    }

    private fun initialisePreview() {
        playerSummaryImageViewModel.playerSummaryImageUiModel
            .hasEvents
            .observe(viewLifecycleOwner) { hasEvents ->
                if (!hasEvents) {
                    binding.imageUrlString = playerSummaryImageViewModel
                        .playerSummaryImageUiModel
                        .mediaImageUrl
                        .value
                }
            }

        Timber.i(
            "image display ${playerSummaryImageViewModel
                .playerSummaryImageUiModel
                .mediaImageUrl
                .value}"
        )

        if (playerSummaryImageViewModel.playerSummaryImageUiModel.hasEvents.value != true) {
            binding.imageUrlString = playerSummaryImageViewModel
                .playerSummaryImageUiModel
                .mediaImageUrl
                .value
            Timber.i(
                "image display ${playerSummaryImageViewModel
                    .playerSummaryImageUiModel
                    .mediaImageUrl
                    .value}"
            )
        }
    }

    private fun setupPreview() {
        playerSummaryImageViewModel
            .playerSummaryImageUiModel
            .podXCallPromptEventsListLiveData
            .observe(viewLifecycleOwner) { eventList ->
                if (eventList.isNotEmpty()) {
                    binding.imageUrlString = ""
                    binding.placeholder = resources.getDrawable(R.drawable.ic_icons_call, activity?.theme)
                    binding.imageviewPlayerSummaryImage.setOnClickListener {
                        val event = eventList.sortedBy { it.timeStart }.last()
                        navigateToCall(event)
                    }
                }
            }

        playerSummaryImageViewModel
            .playerSummaryImageUiModel
            .podXFeedBackEventsListLiveData
            .observe(viewLifecycleOwner) { eventList ->
                if (eventList.isNotEmpty()) {
                    val event = eventList.sortedBy { it.timeStart }.last()
                    binding.imageUrlString = event.ogMetadata.OGImage
                    binding.placeholder = resources.getDrawable(R.drawable.ic_icons_feedback, activity?.theme)
                    binding.imageviewPlayerSummaryImage.setOnClickListener {
                        navigateToFeedBackActivity(event)
                    }
                }
            }

        // playerSummaryImageViewModel
        //     .playerSummaryImageUiModel
        //     .podXFeedLinkEventsListLiveData
        //     .observe(viewLifecycleOwner) { eventList ->
        //         if (eventList.isNotEmpty()) {
        //             val event = eventList.sortedBy { it.timeStart }.last()
        //             binding.imageUrlString = event.remoteFeedImageUrlString
        //             binding.placeholder = resources.getDrawable(R.drawable.ic_icons_podcast, activity?.theme)
        //             binding.imageviewPlayerSummaryImage.setOnClickListener {
        //                 navigateToFeedItem(event)
        //             }
        //         }
        //     }

        playerSummaryImageViewModel
            .playerSummaryImageUiModel
            .podXImageEventsListLiveData
            .observe(viewLifecycleOwner) { eventList ->
                if (eventList.isNotEmpty()) {
                    val event = eventList.sortedBy { it.timeStart }.last()
                    Timber.i("logging new image ")
                    binding.imageUrlString = event.urlString
                    binding.placeholder = resources.getDrawable(R.drawable.ic_icons_image, activity?.theme)
                    binding.imageviewPlayerSummaryImage.setOnClickListener {
                        navigateToImage(event)
                    }
                }
            }

        playerSummaryImageViewModel
            .playerSummaryImageUiModel
            .podXNewsLetterSignUpEventsListLiveData
            .observe(viewLifecycleOwner) { eventList ->
                if (eventList.isNotEmpty()) {
                    val event = eventList.sortedBy { it.timeStart }.last()
                    binding.imageUrlString = event.ogMetadata.OGImage
                    binding.placeholder = resources.getDrawable(R.drawable.ic_icons_newsletter, activity?.theme)
                    binding.imageviewPlayerSummaryImage.setOnClickListener {
                        navigateToNewsLetterSignUpActivity(event)
                    }
                }
            }

        playerSummaryImageViewModel
            .playerSummaryImageUiModel
            .podXPollEventsListLiveData
            .observe(viewLifecycleOwner) { eventList ->
                if (eventList.isNotEmpty()) {
                    val event = eventList.sortedBy { it.timeStart }.last()
                    binding.imageUrlString = event.ogMetadata.OGImage
                    binding.placeholder = resources.getDrawable(R.drawable.ic_icons_poll, activity?.theme)
                    binding.imageviewPlayerSummaryImage.setOnClickListener {
                        navigateToPollActivity(event)
                    }
                }
            }

        playerSummaryImageViewModel
            .playerSummaryImageUiModel
            .podXSocialPromptEventsListLiveData
            .observe(viewLifecycleOwner) { eventList ->
                if (eventList.isNotEmpty()) {
                    val event = eventList.sortedBy { it.timeStart }.last()
                    binding.imageUrlString = event.ogMetadata.OGImage
                    binding.placeholder = resources.getDrawable(R.drawable.ic_icons_social, activity?.theme)
                    binding.imageviewPlayerSummaryImage.setOnClickListener {
                        navigateToSocialPromptActivity(event)
                    }
                }
            }

        playerSummaryImageViewModel
            .playerSummaryImageUiModel
            .podXSupportEventsListLiveData
            .observe(viewLifecycleOwner) { eventList ->
                if (eventList.isNotEmpty()) {
                    val event = eventList.sortedBy { it.timeStart }.last()
                    binding.imageUrlString = event.ogMetadata.OGImage
                    binding.placeholder = resources.getDrawable(R.drawable.ic_icons_link, activity?.theme)
                    binding.imageviewPlayerSummaryImage.setOnClickListener {
                        navigateToSupportActivity(event)
                    }
                }
            }

        playerSummaryImageViewModel
            .playerSummaryImageUiModel
            .podXTextEventsListLiveData
            .observe(viewLifecycleOwner) { eventList ->
                if (eventList.isNotEmpty()) {
                    val event = eventList.sortedBy { it.timeStart }.last()
                    binding.imageUrlString = ""
                    binding.placeholder = resources.getDrawable(R.drawable.ic_icons_article, activity?.theme)
                    binding.imageviewPlayerSummaryImage.setOnClickListener {
                        navigateToText(event)
                    }
                }
            }

        playerSummaryImageViewModel
            .playerSummaryImageUiModel
            .podXWebEventsListLiveData
            .observe(viewLifecycleOwner) { eventList ->
                if (eventList.isNotEmpty()) {
                    val event = eventList.sortedBy { it.timeStart }.last()
                    Timber.i("logging new web ")
                    binding.imageUrlString = event.ogMetadata.OGImage
                    binding.placeholder = resources.getDrawable(R.drawable.ic_icons_link, activity?.theme)
                    binding.imageviewPlayerSummaryImage.setOnClickListener {
                        navigateToWebActivity(event)
                    }
                }
            }
    }

    // private fun navigateToFeedItem(feedItemFromLink: FeedItem?) {
    //     Timber.i("got to navigate function with ${feedItemFromLink?.feedItemAudioUrl}")
    //     if (feedItemFromLink != null) {
    //         podXEventsContainerViewModel.prepareFeedItemForPlayback(feedItemFromLink)
    //
    //         findNavController()
    //             .navigate(R.id.action_global_playerFragment)
    //     }
    // }

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
                .show(fragmentManager, SUMMARY_PHONE_POPUP_TAG)
        }
    }

    companion object {
        const val SUMMARY_PHONE_POPUP_TAG = "summaryPhonePopup"
    }
}
