package com.lazy.newscast.data

import android.content.Context
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceManager @Inject constructor(@ApplicationContext context: Context) {
    private val dataStore = context.createDataStore("user_preferences")

    fun getCountryPref() : Flow<String> = dataStore.data.map { preference ->
        preference[PreferencesKeys.COUNTRY] ?: Country.us.name
    }

    fun getSortOrderPref() : Flow<String> = dataStore.data.map { preference ->
        preference[PreferencesKeys.SORT_ORDER] ?: SortOrder.popularity.name
    }

    suspend fun changeCountryPref(country: Country) {
        dataStore.edit { settings ->
            settings[PreferencesKeys.COUNTRY] = country.name
        }
    }

    suspend fun changeSortOrder(sortOrder: SortOrder) {
        dataStore.edit { settings ->
            settings[PreferencesKeys.SORT_ORDER] = sortOrder.name
        }
    }
}


private object PreferencesKeys {
    val COUNTRY = preferencesKey<String>("country")
    val SORT_ORDER = preferencesKey<String>("sort_order")
}

enum class Country {
    ar,
    de,
    it,
    ru,
    us,
}

enum class SortOrder {
    relevancy,
    popularity,
    publishedAt
}