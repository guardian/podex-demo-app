package com.guardian.core.coretestapplication

import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

/**
 * An Application to allow android test to inject members to Services contained in the core module
 * using the CoreComponent dependency of the [TestCoreComponent].
 */
class CoreComponentTestApplication : Application(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

    override fun onCreate() {
        super.onCreate()

        DaggerTestCoreComponent.builder()
            .bindContext(applicationContext)
            .build()
            .inject(this)
    }
}