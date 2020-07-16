package com.guardian.podx.dagger

import com.guardian.core.dagger.CoreComponent
import com.guardian.podx.PodXDemoApplication
import com.guardian.podx.dagger.scopes.FeatureScope
import dagger.Component
import dagger.android.AndroidInjectionModule

@FeatureScope
@Component(modules = [AndroidInjectionModule::class,
    ActivityModule::class,
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