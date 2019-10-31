package com.guardian.core

import com.guardian.core.mediaplayer.MediaService
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ServicesForTestingModule {
    @ContributesAndroidInjector
    abstract fun mediaService(): MediaService
}