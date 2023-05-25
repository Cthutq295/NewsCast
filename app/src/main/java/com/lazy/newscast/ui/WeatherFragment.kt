package com.lazy.newscast.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.google.android.material.tabs.TabLayoutMediator
import com.lazy.newscast.R
import com.lazy.newscast.adapter.FragmentPagerAdapter
import com.lazy.newscast.databinding.FragmentWeatherBinding

class WeatherFragment : Fragment(R.layout.fragment_weather) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentWeatherBinding.bind(view)
        val fragments = listOf(WeatherTodayFragment(), WeatherWeekFragment())

        binding.vpWeather.adapter = FragmentPagerAdapter(this, fragments)
        TabLayoutMediator(binding.tbWeather, binding.vpWeather) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "TODAY"
                    tab.setIcon(R.drawable.ic_today_24)
                }
                1 -> {
                    tab.text = "3 DAYS"
                    tab.setIcon(R.drawable.ic_week_24)
                }
            }
        }.attach()
    }
}