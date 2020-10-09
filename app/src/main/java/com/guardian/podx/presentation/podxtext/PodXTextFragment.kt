package com.guardian.podx.presentation.podxtext

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
import com.guardian.podx.databinding.LayoutPodxtextfragmentBinding
import com.guardian.podx.utils.lifecycleAwareVar
import javax.inject.Inject

class PodXTextFragment
@Inject constructor(viewModelProviderFactory: ViewModelProvider.Factory) :
    Fragment() {

    private var binding: LayoutPodxtextfragmentBinding by lifecycleAwareVar()

    private val podXTextEvent: PodXTextFragmentArgs by navArgs()

    private val podXTextViewModel: PodXTextViewModel by viewModels {
        viewModelProviderFactory
    }

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

        binding.buttonPodxtextSkipToTimestamp.setOnClickListener {
            podXTextViewModel.skipToTimestamp(podXTextEvent.podXTextEvent.timeStart)
        }

        (activity as AppCompatActivity?)?.setSupportActionBar(binding.toolbarPodxtext)
        (activity as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (requireActivity() as AppCompatActivity?)?.supportActionBar?.title = ""
    }

    private fun setupEventTimeout() {
        val timeOut = podXTextEvent.podXTextEvent.timeEnd
        podXTextViewModel
            .podXTextUiModel
            .playbackTimeLiveData
            .observe(viewLifecycleOwner) { playbackTime ->
                if (playbackTime > timeOut) {
                    findNavController().navigateUp()
                }
            }
    }
}
