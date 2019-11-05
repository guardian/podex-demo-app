package com.guardian.podxdemo.presentation.podximage

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
import com.guardian.podxdemo.R
import com.guardian.podxdemo.databinding.LayoutPodximagefragmentBinding
import com.guardian.podxdemo.utils.lifecycleAwareVar
import timber.log.Timber
import javax.inject.Inject

class PodXImageFragment
@Inject constructor(viewModelProviderFactory: ViewModelProvider.Factory) :
    Fragment() {

    private var binding: LayoutPodximagefragmentBinding by lifecycleAwareVar()

    //private val podXImageEvent: PodXImageFragmentArgs by navArgs()

    private val podXImageViewModel: PodXImageViewModel by viewModels {
        viewModelProviderFactory
    }

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

        //binding.podxImage = podXImageEvent.podXEvent

        podXImageViewModel
            .podXImageUiModel
            .podXEventLiveData
            .observe(this, Observer { podXEvent ->
                Timber.i("podXEvent changed ${podXEvent?.timeEnd}")
//                if (podXEvent != podXImageEvent.podXEvent) {
                    findNavController().navigateUp()
//                }
            })
    }
}