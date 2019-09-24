package com.guardian.core.dagger

import com.guardian.core.search.SearchRepository
import com.guardian.core.search.SearchRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {
    @Binds
    abstract fun bindSearchRepository(searchRepositoryImpl: SearchRepositoryImpl): SearchRepository
}
