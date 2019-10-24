package com.guardian.core.dagger

import com.google.gson.Gson
import com.guardian.core.BuildConfig
import com.guardian.core.library.xml.XmlPullParserAdapter
import com.guardian.core.library.xml.XmlPullParserAdapterImpl
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.xmlpull.v1.XmlPullParserFactory
import retrofit2.CallAdapter
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

@Module
class WebModule {

    @Provides
    fun provideOkHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BASIC
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
        return interceptor
    }

    @Provides
    @Named("rxjava")
    fun provideRxJavaCallAdapterFactory(): CallAdapter.Factory =
        RxJava2CallAdapterFactory.create()

    @Provides
    fun provideGson(): Gson = Gson()

    @Provides
    fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory =
        GsonConverterFactory.create(gson)

    @Provides
    fun provideXMLPullParserFactory(): XmlPullParserFactory =
        XmlPullParserFactory.newInstance()

    @Provides
    fun provideXMLPullParserAdapter(xmlPullParserFactory: XmlPullParserFactory):
            XmlPullParserAdapter = XmlPullParserAdapterImpl(xmlPullParserFactory)
}