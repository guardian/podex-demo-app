package com.guardian.podxdemo.dagger

import com.guardian.podxdemo.presentation.MainActivity
import com.guardian.podxdemo.presentation.player.PlayerActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributePlayerActivity(): PlayerActivity
}
