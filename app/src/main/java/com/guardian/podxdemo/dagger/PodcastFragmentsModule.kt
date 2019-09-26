package com.guardian.podxdemo.dagger

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.guardian.podxdemo.dagger.keys.FragmentKey
import com.guardian.podxdemo.presentation.feed.FeedFragment
import com.guardian.podxdemo.presentation.search.SearchFragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.ClassKey
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
    abstract fun bindsFragmentInjectionFactory(fragmentInjectionFactory: FragmentInjectionFactory)
            : FragmentFactory
}