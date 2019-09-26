package com.guardian.podxdemo.dagger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.guardian.podxdemo.dagger.keys.ViewModelKey
import com.guardian.podxdemo.presentation.feed.FeedFragment
import com.guardian.podxdemo.presentation.feed.FeedViewModel
import com.guardian.podxdemo.presentation.search.SearchViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module
abstract class PodcastViewModelsModule {
    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindsSearchFragment(searchViewModel: SearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FeedViewModel::class)
    abstract fun bindsFeedFragment(feedViewModel: FeedViewModel): ViewModel

    @Binds
    abstract fun bindsViewModelFactory(factory: ViewModelInjectionFactory): ViewModelProvider.Factory
}