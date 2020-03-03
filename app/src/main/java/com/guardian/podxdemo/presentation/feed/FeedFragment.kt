package com.guardian.podxdemo.presentation.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DiffUtil
import com.guardian.core.feeditem.FeedItem
import com.guardian.podxdemo.R
import com.guardian.podxdemo.databinding.LayoutFeedfragmentBinding
import com.guardian.podxdemo.utils.lifecycleAwareVar
import timber.log.Timber
import java.util.concurrent.Executor
import javax.inject.Inject

class FeedFragment
@Inject constructor(
    viewModelProviderFactory: ViewModelProvider.Factory,
    private val executor: Executor
) :
    Fragment() {

    private val feedViewModel: FeedViewModel by viewModels {
        viewModelProviderFactory
    }

    private var binding: LayoutFeedfragmentBinding by lifecycleAwareVar()

    private val args: FeedFragmentArgs by navArgs()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        feedViewModel.setPlaceholderData(args.searchResult)
        feedViewModel.getFeedAndItems(args.searchResult.feedUrlString)

        setupFeedInfoView()
        setupFeedEpisodeList()

        (requireActivity() as AppCompatActivity?)?.setSupportActionBar(binding.toolbarFeed)
        (requireActivity() as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (requireActivity() as AppCompatActivity?)?.supportActionBar?.title = ""
    }

    private fun setupFeedInfoView() {
        feedViewModel.uiModel.feedData.observe(
            viewLifecycleOwner,
            Observer { feed ->
                binding.feed = feed
            }
        )
    }

    private fun setupFeedEpisodeList() {
        binding.recyclerviewFeedResults.adapter = FeedListAdapter(
            callback = object : DiffUtil.ItemCallback<FeedItem>() {
                override fun areItemsTheSame(
                    oldItem: FeedItem,
                    newItem: FeedItem
                ): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(
                    oldItem: FeedItem,
                    newItem: FeedItem
                ): Boolean {
                    return oldItem.title == newItem.title
                }
            },
            executor = executor,
            handleSelection = { feedItem ->
                feedViewModel.prepareFeedItemForPlayback(feedItem)
                val action = FeedFragmentDirections.actionFeedFragmentToPlayerFragment()
                findNavController()
                    .navigate(action)
            },
            handlePlayPause = {feedItem ->
                feedViewModel.attemptPlaybackOrPause(feedItem)

            },
            bindIsPlaying = {feedItem ->
                MutableLiveData<Boolean>().also{ isItemPlayingLiveData ->
                    feedViewModel.uiModel.nowPlayingIdLiveData.observe(
                        viewLifecycleOwner,
                        Observer { nowPlayingId ->
                            val isPlaying = feedViewModel.uiModel.isPlayingLiveData.value ?: false
                            isItemPlayingLiveData
                                .postValue((feedItem.feedItemAudioUrl == nowPlayingId)
                                    && isPlaying)
                        })

                    feedViewModel.uiModel.isPlayingLiveData.observe(
                        viewLifecycleOwner,
                        Observer { isPlaying ->
                            val nowPlayingId = feedViewModel.uiModel.nowPlayingIdLiveData.value
                            isItemPlayingLiveData
                                .postValue((feedItem.feedItemAudioUrl == nowPlayingId)
                                    && isPlaying)
                        })
                }
            }).apply {
                feedViewModel.uiModel.feedItemData.observe(
                    viewLifecycleOwner,
                    Observer { feedItemList ->
                        Timber.i("returned items ${feedItemList.size}")
                        submitList(feedItemList)
                    }
                )
            }
    }
}