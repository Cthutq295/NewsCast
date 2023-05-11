package com.lazy.newscast.mvvm.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.lazy.newscast.R
import com.lazy.newscast.adapter.WeekWeatherAdapter
import com.lazy.newscast.databinding.FragmentWeatherWeekBinding
import com.lazy.newscast.mvvm.viewmodel.WeatherViewModel
import com.lazy.newscast.utils.Resource
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

        viewModel.getForecastWeather()

        viewModel.forecastWeather.observe(viewLifecycleOwner) { response ->

            when (response) {
                is Resource.Success -> {
                    binding.llInternetError.visibility = View.GONE
                    binding.pbLoading.visibility = View.GONE

                    binding.rvWeekWeather.visibility = View.VISIBLE
                    weekAdapter.submitList(response.response.forecast.forecastday)
                }
                is Resource.Loading -> {
                    binding.pbLoading.visibility = View.VISIBLE
                }
                is Resource.Error -> {
                    binding.pbLoading.visibility = View.GONE
                    binding.rvWeekWeather.visibility = View.GONE
                    binding.llInternetError.visibility = View.VISIBLE
                }
            }
        }
    }
}