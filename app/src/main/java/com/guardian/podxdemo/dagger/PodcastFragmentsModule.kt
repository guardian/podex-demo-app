package com.guardian.podxdemo.dagger

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.guardian.podxdemo.dagger.keys.FragmentKey
import com.guardian.podxdemo.presentation.collapsedplayer.CollapsedPlayerFragment
import com.guardian.podxdemo.presentation.collapsedplayerlight.CollapsedPlayerLightFragment
import com.guardian.podxdemo.presentation.feed.FeedFragment
import com.guardian.podxdemo.presentation.player.PlayerFragment
import com.guardian.podxdemo.presentation.podxeventscontainer.PodXEventsContainerFragment
import com.guardian.podxdemo.presentation.podximage.PodXImageFragment
import com.guardian.podxdemo.presentation.search.SearchFragment
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
    @FragmentKey(PodXImageFragment::class)
    abstract fun bindsPodXImageFragment(podXImageFragment: PodXImageFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(PodXEventsContainerFragment::class)
    abstract fun bindsPodXEventsContainterFragment
            (podXEventsContainerFragment: PodXEventsContainerFragment): Fragment

    @Binds
    abstract fun bindsFragmentInjectionFactory(fragmentInjectionFactory: FragmentInjectionFactory):
            FragmentFactory
}