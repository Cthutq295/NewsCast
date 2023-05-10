package com.lazy.newscast.mvvm.viewmodel

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lazy.newscast.data.api.Repository
import com.lazy.newscast.models.weather.current.Weather
import com.lazy.newscast.models.weather.forecast.Forecast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: Repository,
    private val connectivityManager: ConnectivityManager
) : ViewModel() {

    // Might even delete this, since all info can be retrieved from Forecast request
    private val currentWeatherResponse = MutableLiveData<Weather>()
    val currentWeather: LiveData<Weather> get() = currentWeatherResponse

    private val forecastWeatherResponse = MutableLiveData<Forecast>()
    val forecastWeather: LiveData<Forecast> get() = forecastWeatherResponse

    fun getForecastWeather() = viewModelScope.launch {
        if (isConnected()) {
            val response = repository.getForecastWeather("rostov")

            if (response.isSuccessful) {
                forecastWeatherResponse.postValue(response.body())
            }
        }
    }

    // Might even delete this, since all info can be retrieved from Forecast request
    fun getCurrentWeather() = viewModelScope.launch {
        if (isConnected()) {
            val response = repository.getCurrentWeather("rostov")

            if (response.isSuccessful) {
                currentWeatherResponse.postValue(response.body())
            }
        }
    }

    private fun isConnected(): Boolean {
        return connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
    }
}