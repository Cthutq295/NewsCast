package com.lazy.newscast.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.lazy.newscast.data.api.weather.WeatherService
import com.lazy.newscast.models.weather.forecast.Forecastday

class ForecastWeatherSource(
    private val weatherService: WeatherService,
    private val location: String
) :
    PagingSource<Int, Forecastday>() {

    override fun getRefreshKey(state: PagingState<Int, Forecastday>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Forecastday> {
        val currentPage = params.key ?: 1

        return try {
            val response = weatherService.getWeatherForecast(location)
            val data = response.body()?.forecast?.forecastday ?: emptyList()
            LoadResult.Page(
                data = data,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (data.isEmpty()) null else currentPage.plus(1)

            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}