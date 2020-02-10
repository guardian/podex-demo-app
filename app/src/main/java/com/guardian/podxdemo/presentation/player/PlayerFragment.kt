package com.guardian.podxdemo.presentation.player

import android.os.Bundle
import android.support.v4.media.MediaMetadataCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.guardian.core.mediaplayer.extensions.albumArtUri
import com.guardian.core.mediaplayer.extensions.duration
import com.guardian.core.mediaplayer.extensions.title
import com.guardian.podxdemo.R
import com.guardian.podxdemo.databinding.LayoutPlayerfragmentBinding
import com.guardian.podxdemo.utils.lifecycleAwareVar
import com.guardian.podxdemo.utils.toTimestampMSS
import kotlinx.coroutines.sync.Mutex
import javax.inject.Inject

class PlayerFragment
@Inject constructor(viewModelProviderFactory: ViewModelProvider.Factory) :
    Fragment() {

    private val playerViewModel: PlayerViewModel by viewModels {
        viewModelProviderFactory
    }

    private var binding: LayoutPlayerfragmentBinding by lifecycleAwareVar()

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
            .mediaButtonIsPlaying
            .observe(
                this,
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

        playerViewModel
            .playerUiModel
            .mediaPlaybackPositionLiveData.observe(
            this,
            Observer { pos -> binding.playbackPosition = pos.toTimestampMSS(context!!) }
        )

        // Setup UI handlers for buttons
        binding.mediaButton.setOnClickListener {
            playerViewModel.playPause()
        }

        setupSeekBar()
    }

    private fun setupSeekBar() {
        binding.seekbarPlayerPosition
            .max = PROGRESS_MAX

        val seekBarMutex = Mutex(false)

        binding.seekbarPlayerPosition
            .setOnSeekBarChangeListener (
                object: SeekBar.OnSeekBarChangeListener {
                    //ignore the scanning
                    override fun onProgressChanged(
                        seekBar: SeekBar?,
                        progress: Int,
                        fromUser: Boolean
                    ) {}

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {seekBarMutex.tryLock(this)}

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
            .observe(this,
                Observer{ playbackTime ->
                    if (!seekBarMutex.isLocked) {
                        setSeekBarPos(playbackTime)
                    }
                })
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
        //if the metadata isn't set we assume playback hasn't started
        val currentDuration = playerViewModel.playerUiModel
            .mediaMetadataLiveData
            .value
            ?.duration ?: 0

        return (this.toDouble() / PROGRESS_MAX  * currentDuration).toLong()
    }

    private companion object {
        private const val PROGRESS_MAX = 1000
        private const val THIRTY_SECONDS = 30000
        private const val TEN_SECONDS = 10000
    }
}