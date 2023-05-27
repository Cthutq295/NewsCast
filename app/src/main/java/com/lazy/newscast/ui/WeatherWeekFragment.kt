package com.lazy.newscast.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.lazy.newscast.R
import com.lazy.newscast.adapter.WeekWeatherAdapter
import com.lazy.newscast.databinding.FragmentWeatherWeekBinding
import com.lazy.newscast.viewmodel.WeatherViewModel
import com.lazy.newscast.utils.Resource
import com.lazy.newscast.utils.UserLocation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherWeekFragment : Fragment(R.layout.fragment_weather_week) {
    private val viewModel: WeatherViewModel by viewModels()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentWeatherWeekBinding.bind(view)

        val weekAdapter = WeekWeatherAdapter(requireContext())

        binding.apply {
            rvWeekWeather.adapter = weekAdapter
            rvWeekWeather.layoutManager = LinearLayoutManager(requireContext())
            rvWeekWeather.setHasFixedSize(false)
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        getLocation()

        viewModel.getForecastWeather(UserLocation.location)

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

    private fun getLocation() {

        val task = fusedLocationProviderClient.lastLocation

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 101)
            return
        }

        task.addOnSuccessListener {
            if(it != null){
                UserLocation.location = "${it.latitude},${it.longitude}"
            }
        }
    }
}