package com.guardian.podxdemo.presentation.podximage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.guardian.podxdemo.R
import com.guardian.podxdemo.databinding.LayoutPodximagefragmentBinding
import com.guardian.podxdemo.utils.lifecycleAwareVar

class PodXImageFragment :
    Fragment() {

    private var binding: LayoutPodximagefragmentBinding by lifecycleAwareVar()

    private val podXImageEvent: PodXImageFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.layout_podximagefragment,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.podxImage = podXImageEvent.podXEvent
    }
}