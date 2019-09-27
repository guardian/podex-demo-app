package com.guardian.podxdemo.presentation.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.guardian.podxdemo.R
import com.guardian.podxdemo.databinding.LayoutFeedfragmentBinding
import com.guardian.podxdemo.utils.lifecycleAwareLazy
import javax.inject.Inject

class FeedFragment
@Inject constructor(viewModelProviderFactory: ViewModelProvider.Factory)
    : Fragment() {

    val feedViewModel: FeedViewModel by viewModels {
        viewModelProviderFactory
    }

    var binding: LayoutFeedfragmentBinding by lifecycleAwareLazy()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.layout_feedfragment,
            container,
            false
        )

        return binding.root
    }


}