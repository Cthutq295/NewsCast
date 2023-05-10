package com.lazy.newscast.utils

sealed class Resource<T> {
    data class Success<T>(val response: T) : Resource<T>()
    class Loading<T> : Resource<T>()
    class Error<T>(val message: String) : Resource<T>()
}