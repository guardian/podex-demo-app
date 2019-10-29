package com.guardian.podxdemo.presentation.player

import android.os.Bundle
import android.support.v4.media.MediaMetadataCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.guardian.core.mediaplayer.extensions.albumArtUri
import com.guardian.core.mediaplayer.extensions.duration
import com.guardian.core.mediaplayer.extensions.title
import com.guardian.core.podxevent.PodXEvent
import com.guardian.core.podxevent.PodXType
import com.guardian.podxdemo.R
import com.guardian.podxdemo.databinding.LayoutPlayerfragmentBinding
import com.guardian.podxdemo.utils.lifecycleAwareVar
import com.guardian.podxdemo.utils.toTimestampMSS
import timber.log.Timber
import javax.inject.Inject

class PlayerFragment
@Inject constructor(viewModelProviderFactory: ViewModelProvider.Factory) :
    Fragment() {

    private val playerViewModel: PlayerViewModel by viewModels {
        viewModelProviderFactory
    }

    private var binding: LayoutPlayerfragmentBinding by lifecycleAwareVar()

    private val args: PlayerFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.layout_playerfragment,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playerViewModel
            .preparePlayer(args.feedItem.feedItemAudioUrl)

        playerViewModel
            .playerUiModel
            .mediaMetadataLiveData
            .observe(
                this,
                Observer<MediaMetadataCompat> { mediaItem ->
                    binding.title = mediaItem.title
                    binding.description = mediaItem.description.description.toString()
                    binding.artUrlString = mediaItem.albumArtUri.toString()
                    binding.duration = mediaItem.duration.toTimestampMSS(context!!)
                }
            )

        playerViewModel
            .playerUiModel
            .mediaButtonRes
            .observe(
                this,
                Observer { res -> binding.mediaButton.setImageResource(res) }
            )

        playerViewModel
            .playerUiModel
            .mediaPlaybackPositionLiveData.observe(
            this,
            Observer { pos -> binding.playbackPosition = pos.toTimestampMSS(context!!) }
        )

        // Setup UI handlers for buttons
        binding.mediaButton.setOnClickListener {
            playerViewModel.playFromUri(args.feedItem.feedItemAudioUrl)
        }

        playerViewModel.setFeedItem(args.feedItem)

        playerViewModel
            .playerUiModel
            .podXEventLiveData
            .observe(this) { podXEvent: PodXEvent? ->
                if (podXEvent != null) {
                    when (podXEvent.type) {
                        PodXType.IMAGE -> {
                            Timber.i("action on podXEvent image")
                            if (findNavController().currentDestination?.id != R.id.podXImageFragment) {
                                val action =
                                    PlayerFragmentDirections.actionPlayerFragmentToPodXImageFragment(
                                        podXEvent
                                    )
                                findNavController().navigate(action)
                            }
                        }
                        else -> {
                            Timber.i("Reached unsupported podX event")
                        }
                    }
                }
            }
    }
}