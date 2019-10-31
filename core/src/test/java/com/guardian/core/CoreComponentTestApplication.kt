package com.guardian.core

import android.app.Application
import com.guardian.core.dagger.DaggerCoreComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import org.robolectric.TestLifecycleApplication
import java.lang.reflect.Method
import javax.inject.Inject

/**
 * An Application to allow robolectric to inject members to Services contained in the core module
 * using the CoreComponent dependency of the [TestCoreComponent].
 */
class CoreComponentTestApplication: Application(), TestLifecycleApplication, HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

    override fun beforeTest(method: Method?) {
    }

    override fun prepareTest(test: Any?) {
        DaggerTestCoreComponent.builder()
            .coreComponent(
                DaggerCoreComponent.builder()
                    .bindContext(context = baseContext)
                    .build()
            )
            .build()
            .inject(this)
    }

    override fun afterTest(method: Method?) {
    }
}