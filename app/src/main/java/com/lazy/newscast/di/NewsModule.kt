package com.lazy.newscast.di

import android.content.Context
import android.net.ConnectivityManager
import com.lazy.newscast.data.api.news.BASE_URL_NEWS
import com.lazy.newscast.data.api.news.NewsService
import com.lazy.newscast.data.api.weather.BASE_URL_WEATHER
import com.lazy.newscast.data.api.weather.WeatherService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NewsModule {

    @Provides
    @Singleton
    fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Provides
    @Singleton
    fun provideNewsRetrofit(): NewsService = Retrofit.Builder()
        .baseUrl(BASE_URL_NEWS)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(NewsService::class.java)

    @Provides
    @Singleton
    fun providesWeatherRetrofit(): WeatherService = Retrofit.Builder()
        .baseUrl(BASE_URL_WEATHER)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(WeatherService::class.java)
}