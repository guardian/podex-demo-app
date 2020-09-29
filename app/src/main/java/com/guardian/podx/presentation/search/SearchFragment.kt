package com.guardian.podx.presentation.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.transition.ChangeBounds
import androidx.transition.Scene
import androidx.transition.TransitionManager
import com.guardian.core.search.SearchResult
import com.guardian.podx.R
import com.guardian.podx.databinding.LayoutSearchfragmentBinding
import com.guardian.podx.presentation.common.hideKeyboard
import com.guardian.podx.utils.lifecycleAwareVar
import java.util.concurrent.Executor
import javax.inject.Inject

/**
 * The UI for lists of podcast feeds
 */

class SearchFragment
@Inject constructor(
    viewModelProviderFactory: ViewModelProvider.Factory,
    private val executor: Executor
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
        setupTransitions()

        // setup action bar
        (activity as AppCompatActivity?)?.setSupportActionBar(binding.toolbarSearch)
        // todo remove static test data add feed search
        // setHasOptionsMenu(true)

        // initialise search
        if (savedInstanceState == null) {
            searchViewModel.doSearch(getString(R.string.searchfragment_default_term))
            binding.progressbarSearch.show()
        }
    }

    private fun setupTransitions() {
        rootScene = Scene(binding.coordinatorSearch)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_searchfragment, menu)
    }

    private var rootScene: Scene by lifecycleAwareVar()
    private val changeBounds = ChangeBounds()
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.item_searchfragment_search) {
            binding.edittextSearchTerm.visibility = when (binding.edittextSearchTerm.visibility) {
                View.VISIBLE -> {
                    hideKeyboard()
                    View.GONE
                }
                else -> {
                    View.VISIBLE
                }
            }
            TransitionManager.go(rootScene, changeBounds)
        }

        return super.onOptionsItemSelected(item)
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
        binding.recyclerviewSearchResults.layoutManager = GridLayoutManager(context, 3)

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
                .observe(
                    viewLifecycleOwner,
                    Observer { results ->
                        binding.progressbarSearch.hide()
                        this.submitList(results)
                    }
                )
        }
    }
}
