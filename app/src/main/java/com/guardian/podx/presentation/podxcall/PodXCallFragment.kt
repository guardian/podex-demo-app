package com.guardian.podx.presentation.podxcall

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
import com.guardian.podx.databinding.LayoutPodxcallfragmentBinding
import com.guardian.podx.utils.lifecycleAwareVar
import javax.inject.Inject

class PodXCallFragment
@Inject constructor(viewModelProviderFactory: ViewModelProvider.Factory) :
    Fragment() {

    private var binding: LayoutPodxcallfragmentBinding by lifecycleAwareVar()

    private val podXCallFragmentArgs: PodXCallFragmentArgs by navArgs()

    private val podXCallViewModel: PodXCallViewModel by viewModels {
        viewModelProviderFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.layout_podxcallfragment,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.podXCallPromptEvent = podXCallFragmentArgs.podXCallPromptEvent

        binding.buttonPodxcallSkipToTimestamp.setOnClickListener {
            podXCallViewModel.skipToTimestamp(podXCallFragmentArgs.podXCallPromptEvent.timeStart)
        }

        binding.buttonPodxcallMakeCall.setOnClickListener {
            val fragmentManager = activity?.supportFragmentManager
            if (fragmentManager != null) {
                PodXCallDialogFragment(podXCallFragmentArgs.podXCallPromptEvent.phoneNumber)
                    .show(fragmentManager, "callPromptPodX")
            }
        }

        (activity as AppCompatActivity?)?.setSupportActionBar(binding.toolbarPodxcall)
        (activity as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (requireActivity() as AppCompatActivity?)?.supportActionBar?.title = ""

        if (podXCallFragmentArgs.newEventFlag) {
            setupEventTimeout()
        }
    }

    private fun setupEventTimeout() {
        val timeOut = podXCallFragmentArgs.podXCallPromptEvent.timeEnd
        podXCallViewModel
            .podXCallUiModel
            .playbackTimeLiveData
            .observe(viewLifecycleOwner) { playbackTime ->
                if (playbackTime > timeOut) {
                    findNavController().navigateUp()
                }
            }
    }
}
