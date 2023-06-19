package com.lazy.newscast.ui

import android.os.Bundle
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.view.isVisible
import androidx.paging.LoadState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.lazy.newscast.R
import com.lazy.newscast.adapter.CurrentWeatherAdapter
import com.lazy.newscast.databinding.FragmentWeatherTodayBinding
import com.lazy.newscast.viewmodel.WeatherViewModel
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

        viewModel.todayWeather.observe(viewLifecycleOwner){
            currentWeatherAdapter.submitData(viewLifecycleOwner.lifecycle,it)
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