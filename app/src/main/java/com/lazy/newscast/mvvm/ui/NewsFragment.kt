package com.lazy.newscast.mvvm.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.lazy.newscast.R
import com.lazy.newscast.adapter.NewsAdapter
import com.lazy.newscast.databinding.FragmentNewsBinding
import com.lazy.newscast.models.news.Article
import com.lazy.newscast.mvvm.viewmodel.NewsViewModel
import com.lazy.newscast.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsFragment : Fragment(R.layout.fragment_news), NewsAdapter.OnItemClickListener,
    MenuProvider {
    private val viewModel: NewsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentNewsBinding.bind(view)

        val newsAdapter = NewsAdapter(requireContext(), this)

        binding.apply {
            recyclerViewNews.adapter = newsAdapter
            recyclerViewNews.layoutManager = LinearLayoutManager(requireContext())
            recyclerViewNews.setHasFixedSize(true)
        }

        viewModel.getTopHeadline()

        viewModel.news.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    binding.pbLoading.visibility = View.GONE
                    binding.llInternetError.visibility = View.GONE
                    newsAdapter.submitList(response.response.articles)
                }
                is Resource.Loading -> {
                    binding.llInternetError.visibility = View.GONE
                    binding.pbLoading.visibility = View.VISIBLE
                }
                is Resource.Error -> {
                    binding.pbLoading.visibility = View.GONE
                    binding.llInternetError.visibility = View.VISIBLE
                }
            }
        }

        activity?.addMenuProvider(this@NewsFragment)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.top_menu, menu)

        val searchItem = menu.findItem(R.id.itemSearchBar)
        val searchView = searchItem.actionView as SearchView
        if (viewModel.searchQuery.value.isNullOrBlank()) searchView.setQuery(
            viewModel.searchQuery.value,
            true
        )

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    viewModel.searchQuery.value = query
                    viewModel.getEverything()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean = true
        })
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.itemSettings -> {
                val direction = NewsFragmentDirections.actionNewsFragmentToNewsSettingsFragment()
                findNavController().navigate(direction)
                true
            }
            else -> {
                false
            }
        }
    }

    override fun onStop() {
        super.onStop()
        requireActivity().removeMenuProvider(this)
    }

    override fun onItemClick(item: Article) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(item.url))
        startActivity(browserIntent)
    }
}