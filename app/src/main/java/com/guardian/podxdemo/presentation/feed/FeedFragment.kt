package com.guardian.podxdemo.presentation.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DiffUtil
import com.guardian.core.feed.FeedItem
import com.guardian.podxdemo.R
import com.guardian.podxdemo.databinding.LayoutFeedfragmentBinding
import com.guardian.podxdemo.utils.lifecycleAwareLazy
import javax.inject.Inject

class FeedFragment
@Inject constructor(viewModelProviderFactory: ViewModelProvider.Factory) :
    Fragment() {

    val feedViewModel: FeedViewModel by viewModels {
        viewModelProviderFactory
    }

    var binding: LayoutFeedfragmentBinding by lifecycleAwareLazy()

    val args: FeedFragmentArgs by navArgs()

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
        feedViewModel.getFeed(args.searchResult.feedUrlString)

        feedViewModel.feedData.observe(
            viewLifecycleOwner,
            Observer { feed ->
                binding.feed = feed
            }
        )

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
            }
        ).apply {
            feedViewModel.feedData.observe(
                viewLifecycleOwner,
                Observer { feed ->
                    submitList(feed.feedItems)
                }
            )
        }
    }
}