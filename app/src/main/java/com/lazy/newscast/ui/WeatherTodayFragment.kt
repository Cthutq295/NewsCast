package com.lazy.newscast.ui

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.core.view.isVisible
import androidx.paging.LoadState
import com.bumptech.glide.Glide
import com.lazy.newscast.R
import com.lazy.newscast.adapter.CurrentWeatherAdapter
import com.lazy.newscast.databinding.FragmentWeatherTodayBinding
import com.lazy.newscast.viewmodel.WeatherViewModel
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

        viewModel.getCurrentWeather()
        viewModel.currentWeather.observe(viewLifecycleOwner) { response ->
            binding.tvInfo.text = response.body()!!.current.condition.text
            binding.tvTemperature.text = response.body()!!.current.temp_c.toString() + "°C"
            binding.tvWindSpeed.text = "Wind: " + response.body()!!.current.wind_mph.toString()
            Glide.with(requireContext())
                .load("https:" + response.body()!!.current.condition.icon)
                .into(binding.ivWeatherIcon)
        }

        viewModel.todayWeatherByHour.observe(viewLifecycleOwner) { paging ->
            currentWeatherAdapter.submitData(viewLifecycleOwner.lifecycle, paging)
        }

        currentWeatherAdapter.addLoadStateListener { loadState ->
            binding.apply {
                pbLoading.isVisible = loadState.source.refresh is LoadState.Loading
                llWeatherContent.isVisible = loadState.source.refresh is LoadState.NotLoading
                llInternetError.isVisible = loadState.source.refresh is LoadState.Error
            }
        }

        activity?.addMenuProvider(this)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {}

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean = true

    override fun onStop() {
        super.onStop()
        requireActivity().removeMenuProvider(this)
    }
}