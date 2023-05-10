package com.lazy.newscast.mvvm.ui

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.lazy.newscast.R
import com.lazy.newscast.adapter.CurrentWeatherAdapter
import com.lazy.newscast.databinding.FragmentWeatherTodayBinding
import com.lazy.newscast.models.weather.forecast.Hour
import com.lazy.newscast.mvvm.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherTodayFragment : Fragment(R.layout.fragment_weather_today), MenuProvider {
    private val viewModel: WeatherViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentWeatherTodayBinding.bind(view)

        val currentWeatherAdapter = CurrentWeatherAdapter(requireContext())
        binding.apply {
            rvCurrentWeather.adapter = currentWeatherAdapter
            rvCurrentWeather.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            // CANNOT set to TRUE since tab layout somehow affects viewholder so it displays incorrectly items
            rvCurrentWeather.setHasFixedSize(false)
        }

        viewModel.getForecastWeather()
        viewModel.forecastWeather.observe(viewLifecycleOwner) {
            binding.tvInfo.text = it.current.condition.text
            binding.tvTemperature.text = it.current.temp_c.toString() + "°C"
            binding.tvWindSpeed.text = "Wind: " + it.current.wind_mph.toString()

            Glide.with(requireContext())
                .load("https:" + it.current.condition.icon)
                .into(binding.ivWeatherIcon)

            val forecastDate = it.forecast.forecastday[0]
            val hour: List<Hour> = forecastDate.hour

            currentWeatherAdapter.submitList(hour)
        }

        activity?.addMenuProvider(this)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        // menuInflater.inflate(R.menu.top_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean = true

    override fun onStop() {
        super.onStop()
        requireActivity().removeMenuProvider(this)
    }
}