package com.guardian.podxdemo.presentation.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.guardian.core.search.SearchResult
import com.guardian.podxdemo.R
import com.guardian.podxdemo.databinding.LayoutSearchfragmentBinding
import com.guardian.podxdemo.presentation.common.hideKeyboard
import com.guardian.podxdemo.utils.lifecycleAwareVar
import timber.log.Timber
import java.util.concurrent.Executor
import javax.inject.Inject

/**
 * The UI for lists of podcast feeds
 */

class SearchFragment
    @Inject constructor(
        viewModelProviderFactory: ViewModelProvider.Factory,
        val executor: Executor
    ) :
    Fragment() {

    private val searchViewModel: SearchViewModel by viewModels {
        viewModelProviderFactory
    }
    private var binding: LayoutSearchfragmentBinding by lifecycleAwareVar()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.i("Creating Search View")
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.layout_searchfragment,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.lifecycleOwner = viewLifecycleOwner

        setupRecyclerView()
        setupEditText()

        if (savedInstanceState == null) {
            binding.search = getString(R.string.searchfragment_default_term)
            searchViewModel.doSearch(getString(R.string.searchfragment_default_term))
        }
    }

    private fun setupEditText() {
        binding.edittextSearchTerm.setOnEditorActionListener { _, eventId, _ ->
            if (eventId == EditorInfo.IME_ACTION_DONE) {
                val searchTerm: String = binding.search
                    ?: getString(R.string.searchfragment_default_term)
                searchViewModel.doSearch(searchTerm)
                hideKeyboard()

            }

            true
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerviewSearchResults.layoutManager = GridLayoutManager(context, 2)

        binding.recyclerviewSearchResults.adapter = SearchListAdapter(
            callback = object : DiffUtil.ItemCallback<SearchResult>() {
                override fun areItemsTheSame(
                    oldItem: SearchResult,
                    newItem: SearchResult
                ): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(
                    oldItem: SearchResult,
                    newItem: SearchResult
                ): Boolean {
                    return oldItem.feedUrlString == newItem.feedUrlString
                }
            },
            executor = executor
        ) { searchResult ->
            val action = SearchFragmentDirections.actionSearchFragmentToFeedFragment(searchResult)
            findNavController()
                .navigate(action)
        }.apply {
            searchViewModel.uiModel.results
                .observe(viewLifecycleOwner, Observer { results ->
                    this.submitList(results)
                    Timber.i("${this.itemCount} SearchResults added")
                })
        }
    }
}
