package com.lazy.newscast.viewmodel

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.lazy.newscast.data.PreferenceManager
import com.lazy.newscast.data.api.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: Repository,
    private val preferences: PreferenceManager
) : ViewModel() {

    val searchQuery = MutableLiveData("")
    val news = searchQuery.switchMap { queryString ->
        repository.getNews(queryString, preferences).cachedIn(viewModelScope)
    }
}