package com.guardian.podxdemo

import android.app.Application
import com.guardian.core.dagger.DaggerCoreComponent
import com.guardian.podxdemo.dagger.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
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
            .build()
            .inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any>? = dispatchingAndroidInjector
}