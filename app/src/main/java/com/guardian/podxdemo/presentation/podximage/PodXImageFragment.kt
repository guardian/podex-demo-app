package com.guardian.podxdemo.presentation.podximage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.guardian.podxdemo.R
import com.guardian.podxdemo.databinding.LayoutPodximagefragmentBinding
import com.guardian.podxdemo.utils.lifecycleAwareVar
import javax.inject.Inject

class PodXImageFragment
@Inject constructor() :
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

        binding.podxImage = podXImageEvent.podXImageEvent

        // Glide.with(binding.imageviewPodximage)
        //     .load(podXImageEvent.podXImageEvent.urlString)
        //     .into(binding.imageviewPodximage)

        (activity as AppCompatActivity?)?.setSupportActionBar(binding.toolbarPodximage)
        (activity as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (requireActivity() as AppCompatActivity?)?.supportActionBar?.title = ""
    }
}