package com.guardian.podxdemo.presentation.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.podxdemo.R
import com.example.podxdemo.databinding.LayoutSearchfragmentBinding
import com.example.podxdemo.databinding.ViewholderSearchadapterResultBinding
import com.guardian.core.search.SearchResult
import com.guardian.podxdemo.utils.lifecycleAwareLazy
import javax.inject.Inject

class SearchFragment
    @Inject constructor(viewModelProviderFactory: ViewModelProvider.Factory)
    : Fragment() {

    private val searchViewModel: SearchViewModel by viewModels {
        viewModelProviderFactory
    }

    private var binding: LayoutSearchfragmentBinding by lifecycleAwareLazy()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.layout_searchfragment,
            container,
            false
        )
        return inflater.inflate(R.layout.layout_searchfragment, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recyclerviewSearchResults.adapter = SearchAdapter()
            .apply {
                searchViewModel.searchResults
                    .observe(viewLifecycleOwner, Observer { results ->
                        this.submitList(results)
                    })
            }
    }
}

class SearchAdapter()
    : ListAdapter<SearchResult, SearchAdapter.DataBoundViewHolder<ViewholderSearchadapterResultBinding>>
    (object
    : DiffUtil.ItemCallback<SearchResult>() {
    override fun areItemsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean {
        return oldItem.feedUrlString == newItem.feedUrlString
    }

}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBoundViewHolder<ViewholderSearchadapterResultBinding> {
        val binding = DataBindingUtil.inflate<ViewholderSearchadapterResultBinding>(
            LayoutInflater.from(parent.context),
            R.layout.viewholder_searchadapter_result,
            parent,
            false
        )
        return DataBoundViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataBoundViewHolder<ViewholderSearchadapterResultBinding>, position: Int) {
        holder.binding.searchResult = getItem(position)
    }

    class DataBoundViewHolder<out T : ViewDataBinding> constructor(val binding: T) :
        RecyclerView.ViewHolder(binding.root)
}