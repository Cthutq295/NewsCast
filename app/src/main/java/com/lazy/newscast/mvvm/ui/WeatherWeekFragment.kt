package com.lazy.newscast.mvvm.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.lazy.newscast.R
import com.lazy.newscast.adapter.WeekWeatherAdapter
import com.lazy.newscast.databinding.FragmentWeatherWeekBinding
import com.lazy.newscast.mvvm.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherWeekFragment : Fragment(R.layout.fragment_weather_week) {
    private val viewModel: WeatherViewModel by viewModels()

    private var timeStart: Long = 0
    private var timeEnd: Long = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)

        timeStart = System.currentTimeMillis()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        timeEnd = System.currentTimeMillis()

        Log.i("MYDEBUG", " Fragment Load Took: ${timeEnd-timeStart}")


        val binding = FragmentWeatherWeekBinding.bind(view)

        val weekAdapter = WeekWeatherAdapter(requireContext())

        binding.apply {
            rvWeekWeather.adapter = weekAdapter
            rvWeekWeather.layoutManager = LinearLayoutManager(requireContext())
            rvWeekWeather.setHasFixedSize(false)
        }

        viewModel.getForecastWeather()

        viewModel.forecastWeather.observe(viewLifecycleOwner){
            weekAdapter.submitList(it.forecast.forecastday)
        }
    }
}