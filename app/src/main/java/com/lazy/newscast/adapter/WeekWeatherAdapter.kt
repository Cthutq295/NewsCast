package com.lazy.newscast.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lazy.newscast.R
import com.lazy.newscast.databinding.ItemWeekWeatherBinding
import com.lazy.newscast.models.weather.forecast.Forecastday

class WeekWeatherAdapter(val context: Context) :
    ListAdapter<Forecastday, WeekWeatherAdapter.WeekWeatherViewHolder>(DiffCallback()) {
    inner class WeekWeatherViewHolder(private val binding: ItemWeekWeatherBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(forecastDay: Forecastday) {
            binding.apply {
                var monthAndDay = forecastDay.date.substring(5)

                tvInfo.text = forecastDay.day.condition.text
                tvTemperature.text = forecastDay.day.avgtemp_c.toString() + "°C"
                tvDate.text = "Date: $monthAndDay"
                tvWindSpeed.text = "Wind: " + forecastDay.day.maxwind_mph.toString() + "mp/h"
                tvHumidity.text = "Humidity: " + forecastDay.day.avghumidity.toString() + "%"

                Glide.with(context)
                    .load("https:" + forecastDay.day.condition.icon)
                    .error(R.drawable.error_image)
                    .into(ivWeatherIcon)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekWeatherViewHolder {
        val binding =
            ItemWeekWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeekWeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeekWeatherViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    class DiffCallback : DiffUtil.ItemCallback<Forecastday>() {
        override fun areItemsTheSame(oldItem: Forecastday, newItem: Forecastday): Boolean =
            oldItem.date_epoch == newItem.date_epoch


        override fun areContentsTheSame(oldItem: Forecastday, newItem: Forecastday): Boolean =
            oldItem == newItem

    }
}