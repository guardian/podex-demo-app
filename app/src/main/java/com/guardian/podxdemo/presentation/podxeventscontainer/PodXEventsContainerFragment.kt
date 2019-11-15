package com.guardian.podxdemo.presentation.podxeventscontainer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.guardian.core.podxevent.PodXImageEvent
import com.guardian.core.podxevent.PodXWebEvent
import com.guardian.podxdemo.R
import com.guardian.podxdemo.databinding.LayoutPodxeventscontainerfragmentBinding
import com.guardian.podxdemo.utils.lifecycleAwareVar
import timber.log.Timber
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

    private val thumbnailMutableLiveData: MutableLiveData<List<PodXEventThumbnailData>>
        = MutableLiveData(listOf())

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

        feedThumnailLiveData()
        bindVisibilitySwitch()
        setupRecyclerview()

        thumbnailMutableLiveData.observe(viewLifecycleOwner, Observer { Timber.i("${it.size} thumbnails")})
    }

    private fun feedThumnailLiveData() {
        val imageThumbnailData = podXEventsContainerViewModel
            .podXEventsContainerUiModel
            .podXImageEventsListLiveData
            .map { imageList ->
                Timber.i("image list length fed to fumbnails ${imageList.size}")
                imageList.map { image ->
                    image.toPodXEventThumbnail(
                        onClickListener = View.OnClickListener {
                            navigateToImage(image)
                        }
                    )
                }
            }

        val webThumbnailData = podXEventsContainerViewModel
            .podXEventsContainerUiModel
            .podXWebEventsListLiveData
            .map { webList ->
                Timber.i("web list length fed to fumbnails ${webList.size}")
                webList.map { web ->
                    web.toPodXEventThumbnail(
                        onClickListener = View.OnClickListener {
                            navigateToWeb(web)
                        }
                    )
                }
            }

        imageThumbnailData.observe(viewLifecycleOwner) {
            Timber.i("${it.size} thumbnails")
        }

        MediatorLiveData<List<PodXEventThumbnailData>>()
            .apply {
                addSource(imageThumbnailData) {
                    Timber.i("list of showing ${it.size}")
                    value = it + (webThumbnailData.value ?: listOf())
                }

                addSource(webThumbnailData) {
                    value = (imageThumbnailData.value ?: listOf()) + it
                }
            }.observe(viewLifecycleOwner) {
                thumbnailMutableLiveData.postValue(it)
            }
    }

    private fun navigateToWeb(podXWebEvent: PodXWebEvent) {
        val argsBundle = Bundle()
            .apply {
                putParcelable("podXWebEvent", podXWebEvent)
            }

        findNavController()
            .navigate(R.id.action_global_podXWebFragment, argsBundle)
    }

    private fun navigateToImage(podXImageEvent: PodXImageEvent) {
        val argsBundle = Bundle()
            .apply {
                putParcelable("podXImageEvent", podXImageEvent)
            }

        findNavController()
            .navigate(R.id.action_global_podXImageFragment, argsBundle)
    }

    private fun setupRecyclerview() {
        binding
            .recyclerviewPodxeventscontainerEvents
            .layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.HORIZONTAL, false
        )

        binding
            .recyclerviewPodxeventscontainerEvents
            .adapter = PodXEventListAdapter(
            callback = object : DiffUtil.ItemCallback<PodXEventThumbnailData>() {
                override fun areItemsTheSame(oldItem: PodXEventThumbnailData, newItem: PodXEventThumbnailData): Boolean =
                    oldItem == newItem

                override fun areContentsTheSame(oldItem: PodXEventThumbnailData, newItem: PodXEventThumbnailData): Boolean =
                    oldItem.captionString == newItem.captionString
                        && oldItem.imageUrlString == newItem.imageUrlString
            },
            executor = executor
        )
            .apply {
                thumbnailMutableLiveData
                    .observe(viewLifecycleOwner,
                        Observer {
                            submitList(it)
                        })
            }
    }

    private fun bindVisibilitySwitch() {
        thumbnailMutableLiveData
            .observe(viewLifecycleOwner,
                Observer {
                    binding
                        .constraintlayoutPodxeventscontainerRoot
                        .visibility = if (it.isEmpty()) {
                        Timber.i("GONE")
                        View.GONE
                    } else {
                        Timber.i("VISIBLE")
                        View.VISIBLE
                    }
                })
    }
}