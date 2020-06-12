package com.guardian.podxdemo.presentation.podxtext

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.guardian.podxdemo.R
import com.guardian.podxdemo.databinding.LayoutPodxtextfragmentBinding
import com.guardian.podxdemo.utils.lifecycleAwareVar
import javax.inject.Inject

class PodXTextFragment @Inject constructor() :
    Fragment() {

    private var binding: LayoutPodxtextfragmentBinding by lifecycleAwareVar()

    private val podXTextEvent: PodXTextFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.layout_podxtextfragment,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.podxText = podXTextEvent.podXTextEvent

        (activity as AppCompatActivity?)?.setSupportActionBar(binding.toolbarPodxtext)
        (activity as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (requireActivity() as AppCompatActivity?)?.supportActionBar?.title = ""
    }
}