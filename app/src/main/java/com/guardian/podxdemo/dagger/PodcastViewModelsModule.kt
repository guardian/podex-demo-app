package com.guardian.podxdemo.dagger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.guardian.podxdemo.dagger.keys.ViewModelKey
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
    abstract fun bindViewModelFactory(factory: ViewModelInjectionFactory): ViewModelProvider.Factory
}