package com.lazy.newscast.data.api.news

import com.lazy.newscast.data.Country
import com.lazy.newscast.data.SortOrder
import com.lazy.newscast.models.news.News
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

// https://newsapi.org/v2/top-headlines?country=ru&apiKey=766fdbc9ab614a2194a8cbcf19252cbe

const val API_KEY_NEWS: String = "766fdbc9ab614a2194a8cbcf19252cbe"
const val BASE_URL_NEWS: String = "https://newsapi.org"

interface NewsService {

    @GET("/v2/everything")
    suspend fun getEverything(
        @Query("q") query: String = "Игры",
        @Query("page") page: Int = 1,
        @Query("sortBy") sortBy: String = SortOrder.popularity.name,
        @Query("apiKey") apiKey: String = API_KEY_NEWS
    ): Response<News>

    @GET("/v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String = Country.us.name,
        @Query("page") page: Int = 1,
        @Query("apiKey") apiKey: String = API_KEY_NEWS
    ): Response<News>
}