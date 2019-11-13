package com.guardian.podxdemo.presentation.collapsedplayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.guardian.core.mediaplayer.extensions.albumArtUri
import com.guardian.core.mediaplayer.extensions.title
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

        playerViewModel
            .playerUiModel
            .mediaMetadataLiveData
            .observe(this, Observer { mediaMetadataCompat ->
                binding.artUrlString = mediaMetadataCompat.albumArtUri.toString()
                binding.title = mediaMetadataCompat.title
            })

        playerViewModel
            .playerUiModel
            .mediaButtonRes
            .observe(this, Observer { imageId ->
                binding.imagebuttonCollapsedPlayerPlaypause.setImageResource(imageId)
            })

        binding
            .imagebuttonCollapsedPlayerPlaypause
            .setOnClickListener {
                playerViewModel.playPause()
            }

        playerViewModel
            .playerUiModel
            .isPreparedLiveData
            .observe(this, Observer { isPlaying ->
                binding.constraintlayoutCollapsedPlayerRoot.visibility = if (isPlaying) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            })

        binding
            .imageviewCollapsedPlayerCover
            .setOnClickListener {
                findNavController()
                    .navigate(R.id.action_global_playerFragment)
            }
    }
}