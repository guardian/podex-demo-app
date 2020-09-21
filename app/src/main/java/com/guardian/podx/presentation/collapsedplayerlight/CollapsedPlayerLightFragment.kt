package com.guardian.podx.presentation.collapsedplayerlight

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.guardian.core.mediaplayer.extensions.duration
import com.guardian.podx.R
import com.guardian.podx.databinding.LayoutCollapsedplayerlightfragmentBinding
import com.guardian.podx.presentation.player.PlayerViewModel
import com.guardian.podx.utils.lifecycleAwareVar
import com.guardian.podx.utils.toTimestampMSS
import kotlinx.coroutines.sync.Mutex
import javax.inject.Inject

class CollapsedPlayerLightFragment
@Inject constructor(viewModelProviderFactory: ViewModelProvider.Factory) :
    Fragment() {

    private val playerViewModel: PlayerViewModel by viewModels {
        viewModelProviderFactory
    }

    private var binding: LayoutCollapsedplayerlightfragmentBinding by lifecycleAwareVar()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.layout_collapsedplayerlightfragment,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPlayerControls()
        setupSeekBar()
    }

    private fun setupPlayerControls() {
        playerViewModel
            .playerUiModel
            .mediaButtonIsPlaying
            .observe(
                viewLifecycleOwner,
                Observer { isPlaying ->
                    if (isPlaying) {
                        binding.imagebuttonCollapsedPlayerLightPlaypause
                            .setImageResource(R.drawable.baseline_pause_circle_filled_black_48)
                    } else {
                        binding.imagebuttonCollapsedPlayerLightPlaypause
                            .setImageResource(R.drawable.baseline_play_circle_filled_black_48)
                    }
                }
            )

        binding
            .imagebuttonCollapsedPlayerLightPlaypause
            .setOnClickListener {
                playerViewModel.playPause()
            }

        binding
            .imagebuttonCollapsedPlayerLightSeekforwards
            .setOnClickListener {
                val currentTime = playerViewModel.playerUiModel.mediaPlaybackPositionLiveData.value

                if (currentTime != null) {
                    playerViewModel.seekToPosition(currentTime + THIRTY_SECONDS)
                }
            }

        binding
            .imagebuttonCollapsedPlayerLightSeekbackwards
            .setOnClickListener {
                val currentTime = playerViewModel.playerUiModel.mediaPlaybackPositionLiveData.value

                if (currentTime != null) {
                    playerViewModel.seekToPosition(currentTime - TEN_SECONDS)
                }
            }
    }

    private fun setupSeekBar() {
        binding.seekbarCollapsedPlayerLightPosition
            .max = PROGRESS_MAX

        val seekBarMutex = Mutex(false)

        binding.seekbarCollapsedPlayerLightPosition
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
                    binding.textviewCollapsedPlayerLightCurrentTime.text =
                        playbackTime.toTimestampMSS(resources)
                }
            )
    }

    private fun setSeekBarPos(pos: Long) {
        // if metadata is not set assume playback has not started
        val currentDuration = playerViewModel.playerUiModel
            .mediaMetadataLiveData
            .value
            ?.duration ?: 0

        binding.seekbarCollapsedPlayerLightPosition
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
        private const val THIRTY_SECONDS = 30000
        private const val TEN_SECONDS = 10000
        private const val PROGRESS_MAX = 1000
    }
}
