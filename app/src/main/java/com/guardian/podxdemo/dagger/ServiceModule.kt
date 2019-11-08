package com.guardian.podxdemo.dagger

import com.guardian.core.mediaplayer.MediaService
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ServiceModule {
    @ContributesAndroidInjector
    abstract fun contributeMediaService(): MediaService
}