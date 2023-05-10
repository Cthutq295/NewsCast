package com.lazy.newscast.mvvm.viewmodel

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.lazy.newscast.data.PreferenceManager
import com.lazy.newscast.data.api.Repository
import com.lazy.newscast.models.news.News
import com.lazy.newscast.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: Repository,
    private val connectivityManager: ConnectivityManager,
    private val preferences: PreferenceManager
) : ViewModel() {

    private val newsResponse = MutableLiveData<Resource<News>>()
    val news: LiveData<Resource<News>>
        get() = newsResponse

    val page: Int = 1
    val searchQuery = MutableStateFlow<String>("")

    fun getEverything() = viewModelScope.launch {
        if (isConnected()) {
            newsResponse.postValue(Resource.Loading())

            val response = repository.getEverything(
                query = searchQuery.value,
                page = page,
                sortOrder = preferences.getSortOrderPref().first()
            )
            if (response.isSuccessful) {
                response.body().let {
                    newsResponse.postValue(Resource.Success(response.body()!!))
                }
            } else {
                newsResponse.postValue(Resource.Error(response.message()))
            }
        } else {
            newsResponse.postValue(Resource.Error("No internet connection"))
        }
    }

    fun getTopHeadline() = viewModelScope.launch {
        if (isConnected()) {
            newsResponse.postValue(Resource.Loading())

            val response = repository.getTopHeadlines(
                country = preferences.getCountryPref().first(),
                page = page
            )
            if (response.isSuccessful) {
                response.body().let {
                    newsResponse.postValue(Resource.Success(response.body()!!))
                }
            } else {
                newsResponse.postValue(Resource.Error(response.message()))
            }
        } else {
            newsResponse.postValue(Resource.Error("No internet connection"))
        }
    }

    private fun isConnected(): Boolean {
        return connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
    }
}