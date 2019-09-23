package com.guardian.podexdemo

import android.app.Activity
import android.app.Application
import com.guardian.core.dagger.CoreComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class PodExDemoApplication : Application(), HasAndroidInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()
    }

    private val coreComponent: CoreComponent by lazy {
        DaggerAppComponent.create()
            .inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any>? = dispatchingAndroidInjector
}