package com.guardian.core.mediaplayer.daggermocks

import android.content.Context
import com.guardian.core.coretestapplication.AbstractMediaServiceTestModule
import com.guardian.core.coretestapplication.CoreComponentTestApplication
import com.guardian.core.coretestapplication.MediaServiceTestModule
import com.guardian.core.dagger.RoomModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule

@TestScope
@Component(modules = [AndroidInjectionModule::class,
    MediaServiceTestModule::class,
    AbstractMediaServiceTestModule::class,
    RoomModule::class])
interface TestCoreComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun bindContext(context: Context): Builder
        fun build(): TestCoreComponent
    }

    fun inject(coreComponentTestApplication: CoreComponentTestApplication)
}