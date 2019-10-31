package com.guardian.core

import com.guardian.core.dagger.CoreComponent
import dagger.Component
import dagger.android.AndroidInjectionModule

@TestScope
@Component(dependencies = [CoreComponent::class],
    modules = [ServicesForTestingModule::class,
        AndroidInjectionModule::class])
interface TestCoreComponent {
    @Component.Builder
    interface Builder {
        fun coreComponent(coreComponent: CoreComponent): Builder
        fun build(): TestCoreComponent
    }

    fun inject(coreComponentTestApplication: CoreComponentTestApplication)
}