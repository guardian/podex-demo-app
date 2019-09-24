package com.guardian.podxdemo.dagger

import com.guardian.core.dagger.CoreComponent
import com.guardian.podxdemo.PodExDemoApplication
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Component(modules = [AndroidInjectionModule::class,
    MainActivityModule::class,
    PodcastFragmentsModule::class,
    PodcastViewModelsModule::class],
    dependencies = [CoreComponent::class])
interface AppComponent {
    @Component.Builder
    interface Builder {
        fun coreComponent(component: CoreComponent): Builder
        fun build(): AppComponent
    }

    fun inject(podExDemoApplication: PodExDemoApplication)
}