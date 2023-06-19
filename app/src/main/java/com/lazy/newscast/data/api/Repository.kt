package com.lazy.newscast.data.api

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.lazy.newscast.data.PreferenceManager
import com.lazy.newscast.data.api.news.NewsService
import com.lazy.newscast.data.api.weather.WeatherService
import com.lazy.newscast.paging.NewsPagingSource
import com.lazy.newscast.paging.CurrentWeatherPagingSource
import com.lazy.newscast.paging.ForecastWeatherSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val newsService: NewsService,
    private val weatherService: WeatherService
) {

    fun getNews(query: String, settings: PreferenceManager) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                NewsPagingSource(newsService, query, settings)
            }
        ).liveData

    fun getCurrentWeatherForecast(location: String) =
        Pager(
            config = PagingConfig(
                pageSize = 24,
                prefetchDistance = 0,
                enablePlaceholders = true
            ),
            pagingSourceFactory = { CurrentWeatherPagingSource(weatherService, location) }
        ).liveData

    fun getWeekWeatherForecast(location: String) =
        Pager(
            config = PagingConfig(
                pageSize = 3,
                prefetchDistance = 0,
                enablePlaceholders = true
            ),
            pagingSourceFactory = { ForecastWeatherSource(weatherService, location) }
        ).liveData
}