package com.lazy.newscast.viewmodel

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lazy.newscast.data.api.Repository
import com.lazy.newscast.models.weather.forecast.Forecast
import com.lazy.newscast.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: Repository,
    private val connectivityManager: ConnectivityManager
) : ViewModel() {

    private val forecastWeatherResponse = MutableLiveData<Resource<Forecast>>()
    val forecastWeather: LiveData<Resource<Forecast>> get() = forecastWeatherResponse

    fun getForecastWeather() = viewModelScope.launch {
        if (isConnected()) {
            forecastWeatherResponse.postValue(Resource.Loading())
            val response = repository.getForecastWeather("rostov")

            if (response.isSuccessful) {
                response.body().let {
                    forecastWeatherResponse.postValue(Resource.Success(response.body()!!))
                }
            } else {
                forecastWeatherResponse.postValue(Resource.Error(response.message()))
            }
        } else {
            forecastWeatherResponse.postValue(Resource.Error("No Internet Connection"))
        }
    }

    private fun isConnected(): Boolean {
        return connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
    }
}