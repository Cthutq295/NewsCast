package com.lazy.newscast.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.lazy.newscast.R
import com.lazy.newscast.adapter.WeekWeatherAdapter
import com.lazy.newscast.databinding.FragmentWeatherWeekBinding
import com.lazy.newscast.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherWeekFragment : Fragment(R.layout.fragment_weather_week) {
    private val viewModel: WeatherViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentWeatherWeekBinding.bind(view)

        val weekAdapter = WeekWeatherAdapter(requireContext())

        binding.apply {
            rvWeekWeather.adapter = weekAdapter
            rvWeekWeather.layoutManager = LinearLayoutManager(requireContext())
            rvWeekWeather.setHasFixedSize(false)
        }

        viewModel.todayWeatherByHour

        viewModel.weekWeatherByDay.observe(viewLifecycleOwner){
            weekAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        weekAdapter.addLoadStateListener { loadState ->
            binding.apply {
                pbLoading.isVisible = loadState.source.refresh is LoadState.Loading
                rvWeekWeather.isVisible = loadState.source.refresh is LoadState.NotLoading
                llInternetError.isVisible = loadState.source.refresh is LoadState.Error
            }
        }
    }
}