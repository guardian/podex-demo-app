package com.guardian.podexdemo.dagger

import com.guardian.podexdemo.presentation.MainActivity
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity


    @Binds
    @IntoMap
    @ClassKey(MainActivity::class)
    internal abstract fun bindYourAndroidInjectorFactory(factory: MainActivitySubComponent.Factory)
            : AndroidInjector.Factory<Any>
}

