package com.guardian.podxdemo.presentation.podxweb

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.guardian.podxdemo.R
import com.guardian.podxdemo.databinding.LayoutPodxwebfragmentBinding
import com.guardian.podxdemo.utils.lifecycleAwareVar
import javax.inject.Inject

class PodXWebFragment
@Inject constructor() :
    Fragment() {

    private var binding: LayoutPodxwebfragmentBinding by lifecycleAwareVar()

    private val fragmentArgs: PodXWebFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.layout_podxwebfragment,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupWebView()

        binding.podXWeb = fragmentArgs.podXWebEvent
    }

    private fun setupWebView() {
    }
}