package com.lazy.newscast.data.api.weather

import com.lazy.newscast.models.weather.current.Weather
import com.lazy.newscast.models.weather.forecast.Forecast
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

const val API_KEY_WEATHER: String = "a4f80e3e46144efaa8c93627212312 "
const val BASE_URL_WEATHER:String = "http://api.weatherapi.com"

interface WeatherService {
    @GET("/v1/forecast.json?")
    suspend fun getCurrentWeather(
        @Query("q") location: String,
        @Query("key") apiKey: String = API_KEY_WEATHER
    ) : Response<Weather>

    @GET("/v1/forecast.json?")
    suspend fun getWeatherForecast(
        @Query("q") location: String,
        @Query("days") days: Int = 7,
        @Query("key") apiKey: String = API_KEY_WEATHER
    ) : Response<Forecast>
}