package com.lazy.newscast.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.lazy.newscast.data.PreferenceManager
import com.lazy.newscast.data.api.news.NewsService
import com.lazy.newscast.models.news.Article
import kotlinx.coroutines.flow.first

class NewsPagingSource(
    private val newsService: NewsService,
    private val query: String,
    private val settings: PreferenceManager
) : PagingSource<Int, Article>() {

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val currentPage = params.key ?: 1
        return try {
            val response = if (query.isNullOrEmpty() || query.isBlank() || query == "") {
                newsService.getTopHeadlines(settings.getCountryPref().first(), page = currentPage)
            } else {
                newsService.getEverything(query, page = currentPage, settings.getSortOrderPref().toString())
            }

            val data = response.body()?.articles ?: emptyList()

            LoadResult.Page(
                data = data,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (data.isEmpty()) null else currentPage.plus(1)
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
