package com.guardian.podx.dagger

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.guardian.podx.dagger.keys.FragmentKey
import com.guardian.podx.presentation.collapsedplayer.CollapsedPlayerFragment
import com.guardian.podx.presentation.collapsedplayerlight.CollapsedPlayerLightFragment
import com.guardian.podx.presentation.feed.FeedFragment
import com.guardian.podx.presentation.player.PlayerFragment
import com.guardian.podx.presentation.podxeventscontainer.PodXEventsContainerFragment
import com.guardian.podx.presentation.search.SearchFragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class PodcastFragmentsModule {
    @Binds
    @IntoMap
    @FragmentKey(SearchFragment::class)
    abstract fun bindsSearchFragment(searchFragment: SearchFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(FeedFragment::class)
    abstract fun bindsFeedFragment(feedFragment: FeedFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(PlayerFragment::class)
    abstract fun bindsPlayerFragment(playerFragment: PlayerFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(CollapsedPlayerFragment::class)
    abstract fun bindsCollapsedPlayerFragment(collapsedPlayerFragment: CollapsedPlayerFragment):
        Fragment

    @Binds
    @IntoMap
    @FragmentKey(CollapsedPlayerLightFragment::class)
    abstract fun bindsCollapsedPlayerLightFragment
    (collapsedPlayerLightFragment: CollapsedPlayerLightFragment):
        Fragment

    @Binds
    @IntoMap
    @FragmentKey(PodXEventsContainerFragment::class)
    abstract fun bindsPodXEventsContainterFragment
    (podXEventsContainerFragment: PodXEventsContainerFragment): Fragment

    @Binds
    abstract fun bindsFragmentInjectionFactory(fragmentInjectionFactory: FragmentInjectionFactory):
        FragmentFactory
}
