package com.guardian.podxdemo.presentation.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import com.guardian.core.feed.FeedItem
import com.guardian.core.search.SearchResult
import com.guardian.podxdemo.R
import com.guardian.podxdemo.databinding.LayoutFeedfragmentBinding
import com.guardian.podxdemo.presentation.search.SearchListAdapter
import com.guardian.podxdemo.utils.lifecycleAwareLazy
import timber.log.Timber
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments
        if (args != null) {


            binding.searchResult = args.getParcelable("searchResult") as SearchResult?

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
                        return oldItem.episode == newItem.episode
                    }
                }
            ).apply {
                //todo remove test data
                submitList(
                    mutableListOf<FeedItem>().apply {
                        for (episode in 1..100) {
                            this.add(FeedItem("Rohans test","cool","", episode))
                        }
                    }
                )
            }
        }
    }


}