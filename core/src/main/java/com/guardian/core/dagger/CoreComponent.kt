package com.guardian.core.dagger

import com.google.gson.Gson
import dagger.Component
import okhttp3.OkHttpClient
import javax.inject.Singleton

/**
 * Component providing application wide singletons.
 */
@Component(modules = [WebModule::class])
@Singleton
interface CoreComponent {

    @Component.Builder
    interface Builder {
        fun build(): CoreComponent
    }

    fun provideOkHttpClient(): OkHttpClient
    fun provideGson(): Gson
}
