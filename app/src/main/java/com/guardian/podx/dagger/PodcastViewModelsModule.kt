package com.guardian.podx.dagger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.guardian.podx.dagger.keys.ViewModelKey
import com.guardian.podx.presentation.feed.FeedViewModel
import com.guardian.podx.presentation.player.PlayerViewModel
import com.guardian.podx.presentation.podxeventscontainer.PodXEventsContainerViewModel
import com.guardian.podx.presentation.podximage.PodXImageViewModel
import com.guardian.podx.presentation.podxlink.PodXLinkViewModel
import com.guardian.podx.presentation.podxtext.PodXTextViewModel
import com.guardian.podx.presentation.search.SearchViewModel
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
    @ViewModelKey(PodXEventsContainerViewModel::class)
    abstract fun bindsPodXEventsContainterViewModel
    (podXEventsContainerViewModel: PodXEventsContainerViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PodXTextViewModel::class)
    abstract fun bindsPodXTextViewModel
    (podXTextViewModel: PodXTextViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PodXLinkViewModel::class)
    abstract fun bindsPodXLinkViewModel
    (podXLinkViewModel: PodXLinkViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PodXImageViewModel::class)
    abstract fun bindsPodXImageViewModel
    (podXImageViewModel: PodXImageViewModel): ViewModel

    @Binds
    abstract fun bindsViewModelFactory(factory: ViewModelInjectionFactory):
        ViewModelProvider.Factory
}
