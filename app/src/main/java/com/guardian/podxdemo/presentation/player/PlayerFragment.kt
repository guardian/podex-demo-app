package com.guardian.podxdemo.presentation.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.guardian.podxdemo.R
import com.guardian.podxdemo.databinding.LayoutPlayerfragmentBinding
import com.guardian.podxdemo.utils.lifecycleAwareVar
import javax.inject.Inject

class PlayerFragment
@Inject constructor(viewModelProviderFactory: ViewModelProvider.Factory) :
    Fragment() {

    val playerViewModel: PlayerViewModel by viewModels {
        viewModelProviderFactory
    }

    var binding: LayoutPlayerfragmentBinding by lifecycleAwareVar()

    val args: PlayerFragmentArgs by navArgs()

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

        binding.feedItem = args.feedItem

        playerViewModel.playFromUri(args.feedItem.feedItemAudioUrl)
    }
}