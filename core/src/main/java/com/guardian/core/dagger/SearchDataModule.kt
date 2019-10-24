package com.guardian.core.dagger

import com.guardian.core.search.api.itunes.ItunesSearchApi
import com.guardian.core.search.api.spoofedtestfeed.SpoofedTestFeedApi
import com.guardian.core.search.api.spoofedtestfeed.SpoofedTestFeedApiImpl
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

@Module
class SearchDataModule {
    @Provides
    fun provideItunesSearchApi (
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
        @Named("rxjava") rxJavaCallAdapterFactory: CallAdapter.Factory
    ): ItunesSearchApi = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(ItunesSearchApi.ENDPOINT)
            .addCallAdapterFactory(rxJavaCallAdapterFactory)
            .addConverterFactory(gsonConverterFactory)
            .build()
            .create(ItunesSearchApi::class.java)

    //TODO Delete this
    @Provides
    fun provideSpoofedTestFeed(): SpoofedTestFeedApi = SpoofedTestFeedApiImpl()


    // todo some sort of LRU disk cache for recent search results
}