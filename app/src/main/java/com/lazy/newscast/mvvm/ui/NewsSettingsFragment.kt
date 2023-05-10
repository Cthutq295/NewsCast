package com.lazy.newscast.mvvm.ui

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.lazy.newscast.R
import com.lazy.newscast.data.Country
import com.lazy.newscast.data.SortOrder
import com.lazy.newscast.databinding.FragmentNewsSettingsBinding
import com.lazy.newscast.mvvm.viewmodel.NewsSettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsSettingsFragment : Fragment(R.layout.fragment_news_settings) {
    private val viewModel: NewsSettingsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentNewsSettingsBinding.bind(view)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.countryPref.collect { countryString ->
                val countryPref = when (countryString) {
                    Country.ar.name -> 0
                    Country.de.name -> 1
                    Country.it.name -> 2
                    Country.ru.name -> 3
                    Country.us.name -> 4
                    else -> 0
                }
                binding.spCountryList.setSelection(countryPref)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.sortOrderPref.collect { sortOrderString ->
                when (sortOrderString) {
                    SortOrder.publishedAt.name -> binding.rbSortByDate.isChecked = true
                    SortOrder.popularity.name -> binding.rbSortByPopularity.isChecked = true
                    SortOrder.relevancy.name -> binding.rbSortByRelevancy.isChecked = true
                    else -> binding.rbSortByDate.isChecked = true
                }
            }
        }

        setupSpinnerMenu(binding)

        binding.rgSortOptions.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rbSortByDate -> viewModel.updateSortOrderPref(SortOrder.publishedAt)
                R.id.rbSortByPopularity -> viewModel.updateSortOrderPref(SortOrder.popularity)
                R.id.rbSortByRelevancy -> viewModel.updateSortOrderPref(SortOrder.relevancy)
            }
        }
    }

    private fun setupSpinnerMenu(binding: FragmentNewsSettingsBinding) {
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.countries,
            android.R.layout.simple_spinner_item
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spCountryList.adapter = adapter
        binding.spCountryList.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                when (pos) {
                    0 -> viewModel.updateCountryPref(Country.ar)
                    1 -> viewModel.updateCountryPref(Country.de)
                    2 -> viewModel.updateCountryPref(Country.it)
                    3 -> viewModel.updateCountryPref(Country.ru)
                    4 -> viewModel.updateCountryPref(Country.us)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }
}