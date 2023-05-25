package com.lazy.newscast.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lazy.newscast.data.Country
import com.lazy.newscast.data.PreferenceManager
import com.lazy.newscast.data.SortOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsSettingsViewModel @Inject constructor(private val preferences: PreferenceManager) :
    ViewModel() {

    val countryPref = preferences.getCountryPref()
    val sortOrderPref = preferences.getSortOrderPref()

    fun updateCountryPref(country: Country) = viewModelScope.launch {
        preferences.changeCountryPref(country)
    }

    fun updateSortOrderPref(sortOrder: SortOrder) = viewModelScope.launch {
        preferences.changeSortOrder(sortOrder)
    }
}