package com.guardian.podexdemo.dagger

import com.guardian.podexdemo.presentation.MainActivity
import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent
interface MainActivitySubComponent: AndroidInjector<MainActivity> {
    @Subcomponent.Factory
    interface Factory: AndroidInjector.Factory<MainActivity>
}
