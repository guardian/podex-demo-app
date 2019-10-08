package com.guardian.podxdemo

import android.app.Application
import com.guardian.core.dagger.DaggerCoreComponent
import com.guardian.mediaplayer.media.dagger.DaggerMediaPlayerComponent
import com.guardian.podxdemo.dagger.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import timber.log.Timber
import javax.inject.Inject

class PodXDemoApplication : Application(), HasAndroidInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder()
            .coreComponent(
                DaggerCoreComponent.builder()
                    .build()
            )
            .mediaPlayerComponent(
                DaggerMediaPlayerComponent.builder()
                    .bindContext(context = this.applicationContext)
                    .build()
            )
            .build()
            .inject(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Timber.i("DebugTree Planted")
        }
    }

    override fun androidInjector(): AndroidInjector<Any>? = dispatchingAndroidInjector
}