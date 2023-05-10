package com.lazy.newscast.models.weather.forecast

data class Forecast(
    val current: Current,
    val forecast: ForecastX,
    val location: Location
)