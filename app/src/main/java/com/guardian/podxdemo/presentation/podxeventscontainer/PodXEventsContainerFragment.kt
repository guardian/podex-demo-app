package com.guardian.podxdemo.presentation.podxeventscontainer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.guardian.podxdemo.R
import com.guardian.podxdemo.databinding.LayoutPodxeventscontainerfragmentBinding
import com.guardian.podxdemo.utils.lifecycleAwareVar
import javax.inject.Inject

class PodXEventsContainerFragment
@Inject constructor(
    viewModelProviderFactory: ViewModelProvider.Factory
) :
    Fragment() {

    private val podXEventsContainerViewModel: PodXEventsContainerViewModel by viewModels {
        viewModelProviderFactory
    }

    private var binding: LayoutPodxeventscontainerfragmentBinding by lifecycleAwareVar()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.layout_podxeventscontainerfragment,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}