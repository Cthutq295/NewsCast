package com.lazy.newscast.data.api

import com.lazy.newscast.data.api.news.NewsService
import com.lazy.newscast.data.api.weather.WeatherService
import com.lazy.newscast.models.news.News
import com.lazy.newscast.models.weather.forecast.Forecast
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val newsService: NewsService,
    private val weatherService: WeatherService
) {

    suspend fun getEverything(query: String, page: Int, sortOrder: String): Response<News> {
        return newsService.getEverything(query = query, page = page, sortBy = sortOrder)
    }

    suspend fun getTopHeadlines(country: String, page: Int): Response<News> {
        return newsService.getTopHeadlines(country = country, page = page)
    }

    suspend fun getForecastWeather(location: String): Response<Forecast> {
        return weatherService.getWeatherForecast(location)
    }
}