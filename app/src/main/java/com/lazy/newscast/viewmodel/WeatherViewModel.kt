package com.lazy.newscast.viewmodel

import androidx.lifecycle.ViewModel
import com.lazy.newscast.data.api.Repository
import com.lazy.newscast.utils.UserLocation
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: Repository,
) : ViewModel() {

    val todayWeather = repository.getCurrentWeatherForecast(UserLocation.location)
    val weekWeather = repository.getWeekWeatherForecast(UserLocation.location)

}