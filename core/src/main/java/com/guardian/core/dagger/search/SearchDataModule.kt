package com.guardian.core.dagger.search

import com.guardian.core.dagger.scopes.FeatureScope
import com.guardian.core.search.api.ItunesSearchApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@FeatureScope
class SearchDataModule {

    @Provides
    fun provideSearchRemoteDataSource (
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ) : ItunesSearchApi = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(ItunesSearchApi.ENDPOINT)
            .addConverterFactory(gsonConverterFactory)
            .build()
            .create(ItunesSearchApi::class.java)

    //todo some sort of LRU disk cache for recent search results
}