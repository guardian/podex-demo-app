package com.guardian.podx.presentation.player

import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaMetadataCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.navArgs
import com.guardian.core.mediaplayer.extensions.albumArtUri
import com.guardian.core.mediaplayer.extensions.duration
import com.guardian.core.mediaplayer.extensions.title
import com.guardian.podx.R
import com.guardian.podx.databinding.LayoutPlayerfragmentBinding
import com.guardian.podx.utils.lifecycleAwareVar
import com.guardian.podx.utils.toTimestampMSS
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
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

        setupMediaInfo()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupPlayerControls()
        setupSeekBar()
        setupScrollEvent()
        setupTextExpansion()

        (activity as AppCompatActivity?)?.setSupportActionBar(binding.toolbarPlayer)
        (activity as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (requireActivity() as AppCompatActivity?)?.supportActionBar?.title = ""
    }

    private fun setupTextExpansion() {
        val clickEvent = {
            if (binding.textviewPlayTitle.maxLines == 1) {
                binding.textviewPlayTitle.maxLines = 10
                binding.textviewPlaySubtitle.maxLines = 50
            } else {
                binding.textviewPlayTitle.maxLines = 1
                binding.textviewPlaySubtitle.maxLines = 3
            }
        }
        binding.textviewPlayTitle.setOnClickListener {
            clickEvent()
        }
        binding.textviewPlaySubtitle.setOnClickListener {
            clickEvent()
        }
    }

    private fun setupScrollEvent() {
        if (args.scrollToEvents) {
            // delay the scroll
            lifecycle.coroutineScope.launch {
                delay(600)
                Timber.i("firing full scroll")
                binding.scrollviewPlayerRoot.fullScroll(
                    ScrollView.FOCUS_DOWN
                )
            }
        }
    }

    private fun setupPlayerControls() {
        playerViewModel
            .playerUiModel
            .mediaButtonIsPlaying
            .observe(
                viewLifecycleOwner,
                Observer { isPlaying ->
                    if (isPlaying) {
                        binding.mediaButton
                            .setImageResource(R.drawable.baseline_pause_circle_filled_black_48)
                    } else {
                        binding.mediaButton
                            .setImageResource(R.drawable.baseline_play_circle_filled_black_48)
                    }
                }
            )

        // Setup UI handlers for buttons
        binding.mediaButton.setOnClickListener {
            playerViewModel.playPause()
        }

        binding.buttonPlayerRewindTen.setOnClickListener {
            val currentTime = playerViewModel.playerUiModel.mediaPlaybackPositionLiveData.value

            if (currentTime != null) {
                playerViewModel.seekToPosition(currentTime - TEN_SECONDS)
            }
        }

        binding.buttonPlayerFastForwardThirty.setOnClickListener {
            val currentTime = playerViewModel.playerUiModel.mediaPlaybackPositionLiveData.value

            if (currentTime != null) {
                playerViewModel.seekToPosition(currentTime + THIRTY_SECONDS)
            }
        }
    }

    private fun setupMediaInfo() {

        playerViewModel
            .playerUiModel
            .mediaMetadataLiveData
            .observe(
                viewLifecycleOwner,
                Observer<MediaMetadataCompat> { mediaItem ->
                    if (mediaItem.title != null) {
                        binding.viewPlayTitlePlaceholder.visibility = View.GONE
                        binding.viewPlaySubtitlePlaceholder.visibility = View.GONE
                        binding.title = mediaItem.title.toString()
                        binding.description = mediaItem.description.description.toString()
                    }
                    binding.duration = mediaItem.duration.toTimestampMSS(resources)

                    if (mediaItem.albumArtUri != Uri.EMPTY) {
                        binding.imageUrl = mediaItem.albumArtUri.toString()
                    }
                }
            )

        playerViewModel
            .playerUiModel
            .mediaPlaybackPositionLiveData.observe(
                viewLifecycleOwner,
                Observer { pos -> binding.playbackPosition = pos.toTimestampMSS(resources) }
            )
    }

    private fun setupSeekBar() {
        binding.seekbarPlayerPosition
            .max = PROGRESS_MAX

        val seekBarMutex = Mutex(false)

        binding.seekbarPlayerPosition
            .setOnSeekBarChangeListener(
                object : SeekBar.OnSeekBarChangeListener {
                    // ignore the scanning
                    override fun onProgressChanged(
                        seekBar: SeekBar?,
                        progress: Int,
                        fromUser: Boolean
                    ) {}

                    override fun onStartTrackingTouch(seekBar: SeekBar?) { seekBarMutex.tryLock(this) }

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {
                        seekBarMutex.unlock(this)
                        val seekToIndex = seekBar?.progress?.toTimeInCurrentMedia()
                        if (seekToIndex != null) {
                            playerViewModel.seekToPosition(seekToIndex)
                        }
                    }
                }
            )

        playerViewModel
            .playerUiModel
            .mediaPlaybackPositionLiveData
            .observe(
                viewLifecycleOwner,
                Observer { playbackTime ->
                    if (!seekBarMutex.isLocked) {
                        setSeekBarPos(playbackTime)
                    }
                    binding.textviewPlayerCurrentTime.text = playbackTime.toTimestampMSS(resources)
                }
            )
    }

    private fun setSeekBarPos(pos: Long) {
        // if metadata is not set assume playback has not started
        val currentDuration = playerViewModel.playerUiModel
            .mediaMetadataLiveData
            .value
            ?.duration ?: 0

        binding.seekbarPlayerPosition
            .progress = (pos.toDouble() / currentDuration * PROGRESS_MAX).toInt()
    }

    fun Int.toTimeInCurrentMedia(): Long {
        // if the metadata isn't set we assume playback hasn't started
        val currentDuration = playerViewModel.playerUiModel
            .mediaMetadataLiveData
            .value
            ?.duration ?: 0

        return (this.toDouble() / PROGRESS_MAX * currentDuration).toLong()
    }

    private companion object {
        public const val PROGRESS_MAX = 1000
        private const val THIRTY_SECONDS = 30000
        private const val TEN_SECONDS = 10000
    }
}
