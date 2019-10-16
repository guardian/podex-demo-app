package com.guardian.podxdemo.dagger

import com.guardian.core.dagger.CoreComponent
import com.guardian.podxdemo.PodXDemoApplication
import com.guardian.podxdemo.dagger.scopes.FeatureScope
import dagger.Component
import dagger.android.AndroidInjectionModule

@FeatureScope
@Component(modules = [AndroidInjectionModule::class,
    MainActivityModule::class,
    ServiceModule::class,
    PodcastFragmentsModule::class,
    PodcastViewModelsModule::class,
    AppExecutorsModule::class],
    dependencies = [CoreComponent::class])
interface AppComponent {
    @Component.Builder
    interface Builder {
        fun coreComponent(component: CoreComponent): Builder
        fun build(): AppComponent
    }

    fun inject(podXDemoApplication: PodXDemoApplication)
}