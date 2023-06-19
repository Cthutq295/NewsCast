package com.lazy.newscast.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.lazy.newscast.data.api.weather.WeatherService
import com.lazy.newscast.models.weather.forecast.Hour

class CurrentWeatherPagingSource(
    private val weatherService: WeatherService,
    private val location: String
) :
    PagingSource<Int, Hour>() {

    override fun getRefreshKey(state: PagingState<Int, Hour>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Hour> {
        val currentPage = params.key ?: 1

        return try {
            val response = weatherService.getWeatherForecast(location)

            val forecastDate = response.body()?.forecast?.forecastday?.get(0)
            val hour: List<Hour> = forecastDate?.hour ?: emptyList()

            LoadResult.Page(
                data = hour,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (hour.isEmpty()) null else currentPage.plus(1)
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}