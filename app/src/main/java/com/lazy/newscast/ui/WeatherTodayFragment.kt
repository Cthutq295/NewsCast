package com.lazy.newscast.ui

import android.os.Bundle
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import android.Manifest
import android.content.pm.PackageManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.lazy.newscast.R
import com.lazy.newscast.adapter.CurrentWeatherAdapter
import com.lazy.newscast.databinding.FragmentWeatherTodayBinding
import com.lazy.newscast.models.weather.forecast.Hour
import com.lazy.newscast.viewmodel.WeatherViewModel
import com.lazy.newscast.utils.Resource
import com.lazy.newscast.utils.UserLocation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherTodayFragment : Fragment(R.layout.fragment_weather_today), MenuProvider {
    private val viewModel: WeatherViewModel by viewModels()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

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

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        getLocation()

        viewModel.getForecastWeather(UserLocation.location)

        viewModel.forecastWeather.observe(viewLifecycleOwner) { response ->

            when (response) {
                is Resource.Success -> {
                    binding.pbLoading.visibility = View.GONE
                    binding.llInternetError.visibility = View.GONE

                    binding.llWeatherContent.visibility = View.VISIBLE
                    binding.tvInfo.text = response.response.current.condition.text
                    binding.tvTemperature.text = response.response.current.temp_c.toString() + "°C"
                    binding.tvWindSpeed.text =
                        "Wind: " + response.response.current.wind_mph.toString()

                    Glide.with(requireContext())
                        .load("https:" + response.response.current.condition.icon)
                        .into(binding.ivWeatherIcon)

                    val forecastDate = response.response.forecast.forecastday[0]
                    val hour: List<Hour> = forecastDate.hour

                    currentWeatherAdapter.submitList(hour)
                }
                is Resource.Loading -> {
                    binding.pbLoading.visibility = View.VISIBLE
                    binding.llInternetError.visibility = View.GONE
                    binding.llWeatherContent.visibility = View.GONE
                }
                is Resource.Error -> {
                    binding.pbLoading.visibility = View.GONE
                    binding.llInternetError.visibility = View.VISIBLE
                    binding.llWeatherContent.visibility = View.GONE
                }
            }
        }

        activity?.addMenuProvider(this)
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
                UserLocation.location
            }
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {}

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean = true

    override fun onStop() {
        super.onStop()
        requireActivity().removeMenuProvider(this)
    }
}