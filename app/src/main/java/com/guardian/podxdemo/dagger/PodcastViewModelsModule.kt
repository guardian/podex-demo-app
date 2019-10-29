package com.guardian.podxdemo.dagger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.guardian.podxdemo.dagger.keys.ViewModelKey
import com.guardian.podxdemo.presentation.feed.FeedViewModel
import com.guardian.podxdemo.presentation.player.PlayerViewModel
import com.guardian.podxdemo.presentation.podximage.PodXImageViewModel
import com.guardian.podxdemo.presentation.search.SearchViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class PodcastViewModelsModule {
    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindsSearchViewModel(searchViewModel: SearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FeedViewModel::class)
    abstract fun bindsFeedViewModel(feedViewModel: FeedViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PlayerViewModel::class)
    abstract fun bindsPlayerViewModel(playerViewModel: PlayerViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PodXImageViewModel::class)
    abstract fun bindxPodXImageViewModel(podXImageViewModel: PodXImageViewModel): ViewModel

    @Binds
    abstract fun bindsViewModelFactory(factory: ViewModelInjectionFactory):
        ViewModelProvider.Factory
}