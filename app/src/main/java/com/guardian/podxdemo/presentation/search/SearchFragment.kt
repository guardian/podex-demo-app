package com.guardian.podxdemo.presentation.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.podexdemo.R
import javax.inject.Inject

class SearchFragment
    @Inject constructor(viewModelProviderFactory: ViewModelProvider.Factory)
    : Fragment() {
    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    val searchViewModel: SearchViewModel by viewModels {
        viewModelProviderFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_searchfragment, container)
    }
}

//class SearchAdapter(binding: ViewDataBinding): ListAdapter<SearchResult, SearchAdapter.SearchViewHolder>() {
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun getItemCount(): Int {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//    }
//}