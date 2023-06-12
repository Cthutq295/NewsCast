package com.lazy.newscast.data.api

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.lazy.newscast.data.PreferenceManager
import com.lazy.newscast.data.SortOrder
import com.lazy.newscast.data.api.news.NewsService
import com.lazy.newscast.data.api.weather.WeatherService
import com.lazy.newscast.models.news.News
import com.lazy.newscast.models.weather.forecast.Forecast
import com.lazy.newscast.paging.NewsPagingSource
import retrofit2.Response
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
                pageSize =  20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                NewsPagingSource(newsService, query, settings)
            }
        ).liveData

//    suspend fun getEverything(query: String, page: Int, sortOrder: String = SortOrder.popularity.name): Response<News> {
//        return newsService.getEverything(query = query, page = page, sortBy = sortOrder)
//    }
//
//    suspend fun getTopHeadlines(country: String, page: Int): Response<News> {
//        return newsService.getTopHeadlines(country = country, page = page)
//    }

    suspend fun getForecastWeather(location: String): Response<Forecast> {
        return weatherService.getWeatherForecast(location)
    }
}