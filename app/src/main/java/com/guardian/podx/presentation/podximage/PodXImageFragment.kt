package com.guardian.podx.presentation.podximage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.guardian.podx.R
import com.guardian.podx.databinding.LayoutPodximagefragmentBinding
import com.guardian.podx.utils.lifecycleAwareVar
import javax.inject.Inject

class PodXImageFragment
@Inject constructor(viewModelProviderFactory: ViewModelProvider.Factory) :
    Fragment() {

    private var binding: LayoutPodximagefragmentBinding by lifecycleAwareVar()

    private val podXImageEvent: PodXImageFragmentArgs by navArgs()

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

        binding.podxImage = podXImageEvent.podXImageEvent

        binding.buttonPodximageSkipToTimestamp.setOnClickListener {
            podXImageViewModel.skipToTimestamp(podXImageEvent.podXImageEvent.timeStart)
        }

        (activity as AppCompatActivity?)?.setSupportActionBar(binding.toolbarPodximage)
        (activity as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (requireActivity() as AppCompatActivity?)?.supportActionBar?.title = ""

        if (podXImageEvent.newEventFlag) {
            setupEventTimeout()
        }
    }

    private fun setupEventTimeout() {
        val timeOut = podXImageEvent.podXImageEvent.timeEnd
        podXImageViewModel
            .podXImageUiModel
            .playbackTimeLiveData
            .observe(viewLifecycleOwner) { playbackTime ->
                if (playbackTime > timeOut) {
                    findNavController().navigateUp()
                }
            }
    }
}
