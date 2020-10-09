package com.guardian.podx.dagger

import dagger.Module
import dagger.Provides
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@Module
class AppExecutorsModule {
    @Provides
    fun IOExecutor(): Executor = Executors.newFixedThreadPool(4)
}
