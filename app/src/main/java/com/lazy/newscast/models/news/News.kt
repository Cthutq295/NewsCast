package com.lazy.newscast.models.news

data class News(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)