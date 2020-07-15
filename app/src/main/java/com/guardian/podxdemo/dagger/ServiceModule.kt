package com.guardian.podxdemo.dagger

import com.guardian.core.mediaplayer.MediaService
import com.guardian.podxdemo.service.notification.EventNotificationService
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ServiceModule {
    @ContributesAndroidInjector
    abstract fun contributeMediaService(): MediaService

    @ContributesAndroidInjector
    abstract fun contributeEventNotificationService(): EventNotificationService
}