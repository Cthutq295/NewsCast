package com.lazy.newscast.models.news

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

data class Article(
    val author: String,
    val content: Any,
    val description: Any,
    val publishedAt: String,
    val source: Source,
    val title: String,
    val url: String,
    val urlToImage: Any
){
    fun getTimeDiffFromNow(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        val publishedTime = dateFormat.parse(publishedAt)?.time ?: return ""

        val currentTime = System.currentTimeMillis()
        val diff = currentTime - publishedTime

        val hours = TimeUnit.MILLISECONDS.toHours(diff)
        val days = TimeUnit.MILLISECONDS.toDays(diff)

        return when {
            days > 0 -> "$days ${if (days == 1L) "день" else "дней"} назад"
            hours > 0 -> "$hours ${if (hours == 1L) "час" else "часов"} назад"
            else -> "недавно"
        }
    }


}