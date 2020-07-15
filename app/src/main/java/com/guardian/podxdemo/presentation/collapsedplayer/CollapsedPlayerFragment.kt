package com.guardian.podxdemo.presentation.collapsedplayer

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.guardian.core.mediaplayer.extensions.albumArtUri
import com.guardian.core.mediaplayer.extensions.duration
import com.guardian.podxdemo.R
import com.guardian.podxdemo.databinding.LayoutCollapsedplayerfragmentBinding
import com.guardian.podxdemo.presentation.player.PlayerViewModel
import com.guardian.podxdemo.utils.lifecycleAwareVar
import javax.inject.Inject

class CollapsedPlayerFragment
@Inject constructor(viewModelProviderFactory: ViewModelProvider.Factory) :
    Fragment() {

    private val playerViewModel: PlayerViewModel by viewModels {
        viewModelProviderFactory
    }

    private var binding: LayoutCollapsedplayerfragmentBinding by lifecycleAwareVar()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.layout_collapsedplayerfragment,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRootView()
        setupAlbumArt()
        setupProgressBar()
        setupPlayerControls()
        setupEventButton()
    }

    private fun setupRootView() {
        playerViewModel
            .playerUiModel
            .isPreparedLiveData
            .observe(viewLifecycleOwner, Observer { isPlaying ->
                binding.constraintlayoutCollapsedPlayerRoot.visibility = if (isPlaying) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            })

        binding
            .progressbarCollapsedPlayer
            .setOnClickListener {
                findNavController()
                    .navigate(R.id.action_global_playerFragment)
            }

        binding
            .imageviewCollapsedPlayerCover
            .setOnClickListener {
                findNavController()
                    .navigate(R.id.action_global_playerFragment)
            }
    }

    private fun setupAlbumArt() {
        playerViewModel
            .playerUiModel
            .mediaMetadataLiveData
            .observe(viewLifecycleOwner, Observer { mediaMetadataCompat ->
                binding.artUrlString = mediaMetadataCompat.albumArtUri.toString()
            })

    }

    private fun setupProgressBar() {
        playerViewModel.playerUiModel
            .mediaPlaybackPositionLiveData
            .observe(
                viewLifecycleOwner, Observer {
                    setProgressBarPos(it)
                }
            )

        binding.progressbarCollapsedPlayer.max = PROGRESS_MAX
    }

    private fun setupPlayerControls() {
        playerViewModel
            .playerUiModel
            .mediaButtonIsPlaying
            .observe(viewLifecycleOwner, Observer { isPlaying ->
                if (isPlaying) {
                    binding.imagebuttonCollapsedPlayerPlaypause
                        .setImageResource(R.drawable.baseline_pause_white_36)
                } else {
                    binding.imagebuttonCollapsedPlayerPlaypause
                        .setImageResource(R.drawable.baseline_play_arrow_white_36)
                }
            })

        binding
            .imagebuttonCollapsedPlayerPlaypause
            .setOnClickListener {
                playerViewModel.playPause()
            }

        binding
            .imagebuttonCollapsedPlayerSeekforwards
            .setOnClickListener {
                val currentTime = playerViewModel.playerUiModel.mediaPlaybackPositionLiveData.value

                if (currentTime != null) {
                    playerViewModel.seekToPosition(currentTime + THIRTY_SECONDS)
                }
            }

        binding
            .imagebuttonCollapsedPlayerSeekbackwards
            .setOnClickListener {
                val currentTime = playerViewModel.playerUiModel.mediaPlaybackPositionLiveData.value

                if (currentTime != null) {
                    playerViewModel.seekToPosition(currentTime - TEN_SECONDS)
                }
            }

    }

    private fun setupEventButton() {
        playerViewModel.playerUiModel
            .hasPodXEventsLiveData
            .observe(viewLifecycleOwner, Observer{ hasPodXEvents: Boolean ->
                if (hasPodXEvents) {
                    binding.imagebuttonCollapsedPlayerPodxevents
                        .imageTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(requireContext(), R.color.highlightColor)
                    )
                } else {
                    binding.imagebuttonCollapsedPlayerPodxevents
                        .imageTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(requireContext(), R.color.transparentPrimary)
                    )
                }
            })

        binding.imagebuttonCollapsedPlayerPodxevents
            .setOnClickListener {
                val argsBundle = Bundle().apply {
                    if (playerViewModel
                            .playerUiModel
                            .hasPodXEventsLiveData
                            .value == true) {
                        this.putBoolean("scrollToEvents", true)
                    }
                }
                findNavController()
                    .navigate(R.id.action_global_playerFragment, argsBundle)
            }
    }

    private fun setProgressBarPos(pos: Long) {
        // if metadata is not set assume playback has not started
        val currentDuration = playerViewModel.playerUiModel
            .mediaMetadataLiveData
            .value
            ?.duration ?: 0

        binding.progressbarCollapsedPlayer
            .progress = (pos.toDouble() / currentDuration * PROGRESS_MAX).toInt()
    }

    private companion object {
        private const val PROGRESS_MAX = 1000
        private const val THIRTY_SECONDS = 30000
        private const val TEN_SECONDS = 10000
    }
}