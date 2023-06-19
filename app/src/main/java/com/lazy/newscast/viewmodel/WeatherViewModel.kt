package com.lazy.newscast.viewmodel

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.lazy.newscast.data.api.Repository
import com.lazy.newscast.models.weather.forecast.Forecast
import com.lazy.newscast.utils.UserLocation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: Repository,
    private val connectivityManager: ConnectivityManager
) : ViewModel() {

    val todayWeatherByHour = repository.getCurrentWeatherForecast(UserLocation.location).cachedIn(viewModelScope)
    val weekWeatherByDay = repository.getWeekWeatherForecast(UserLocation.location).cachedIn(viewModelScope)

    private val currentWeatherResponse = MutableLiveData<Response<Forecast>>()
    val currentWeather get() = currentWeatherResponse

    fun getCurrentWeather() = viewModelScope.launch {
        if (isConnected()) {
            val response = repository.getForecastWeather(UserLocation.location)
            if (response.isSuccessful) {
                currentWeatherResponse.postValue(response)
            }
        }
    }

    private fun isConnected(): Boolean {
        return connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
    }
}