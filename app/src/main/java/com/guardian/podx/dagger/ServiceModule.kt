package com.guardian.podx.dagger

import com.guardian.core.mediaplayer.MediaService
import com.guardian.podx.service.notification.EventNotificationService
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ServiceModule {
    @ContributesAndroidInjector
    abstract fun contributeMediaService(): MediaService

    @ContributesAndroidInjector
    abstract fun contributeEventNotificationService(): EventNotificationService
}
