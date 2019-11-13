package com.guardian.podxdemo.presentation.podxeventscontainer

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
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.guardian.core.podxevent.PodXImageEvent
import com.guardian.podxdemo.R
import com.guardian.podxdemo.databinding.LayoutPodxeventscontainerfragmentBinding
import com.guardian.podxdemo.utils.lifecycleAwareVar
import java.util.concurrent.Executor
import javax.inject.Inject

class PodXEventsContainerFragment
@Inject constructor(
    viewModelProviderFactory: ViewModelProvider.Factory,
    private val executor: Executor
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


        podXEventsContainerViewModel
            .podXEventsContainerUiModel
            .podXImageEventsListLiveData
            .observe(viewLifecycleOwner,
                Observer {
                    binding
                        .constraintlayoutPodxeventscontainerRoot
                        .visibility = if (it.isEmpty()) {
                                View.GONE
                            } else {
                                View.VISIBLE
                            }
                })


        binding
            .recyclerviewPodxeventscontainerEvents
            .layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.HORIZONTAL, false
        )

        binding
            .recyclerviewPodxeventscontainerEvents
            .adapter = PodXEventListAdapter(
            callback = object: DiffUtil.ItemCallback<PodXImageEvent>() {
                override fun areItemsTheSame(oldItem: PodXImageEvent, newItem: PodXImageEvent): Boolean =
                    oldItem == newItem

                override fun areContentsTheSame(oldItem: PodXImageEvent, newItem: PodXImageEvent): Boolean =
                    oldItem.timeStart == newItem.timeStart
                        && oldItem.timeEnd == newItem.timeEnd
                        && oldItem.urlString == newItem.urlString
                        && oldItem.type == newItem.type
                        && oldItem.caption == newItem.caption
            },
            executor = executor
        ) { podXEvent ->
            val argsBundle = Bundle()
                .apply {
                    putParcelable("podXImageEvent", podXEvent)
                }

            findNavController()
                .navigate(R.id.action_global_podXImageFragment, argsBundle)
        }
            .apply {
                podXEventsContainerViewModel
                    .podXEventsContainerUiModel
                    .podXImageEventsListLiveData
                    .observe(viewLifecycleOwner,
                        Observer { this.submitList(it) })
            }
    }

}