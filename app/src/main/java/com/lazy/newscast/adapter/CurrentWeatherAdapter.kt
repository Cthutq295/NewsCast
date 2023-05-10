package com.lazy.newscast.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lazy.newscast.R
import com.lazy.newscast.databinding.ItemCurrentWeatherBinding
import com.lazy.newscast.models.weather.forecast.Hour

class CurrentWeatherAdapter(private val context: Context) :
    ListAdapter<Hour, CurrentWeatherAdapter.CurrentWeatherViewHolder>(DiffCallback()) {

    inner class CurrentWeatherViewHolder(private val binding: ItemCurrentWeatherBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(current: Hour) {
            val hoursAndMinutes = current.time.substring(11) // извлечение символов, начиная с 11-го индекса

            binding.tvInfo.text = current.condition.text
            binding.tvTime.text = hoursAndMinutes
            binding.tvTemperature.text = current.temp_c.toString() + "°C"
            binding.tvWindSpeed.text = "Wind: " + current.wind_mph.toString()

            Glide.with(context)
                .load("https:" + current.condition.icon)
                .error(R.drawable.error_image)
                .into(binding.ivWeatherIcon)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrentWeatherViewHolder {
        val binding =
            ItemCurrentWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CurrentWeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrentWeatherViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    class DiffCallback : DiffUtil.ItemCallback<Hour>() {
        override fun areItemsTheSame(oldItem: Hour, newItem: Hour): Boolean {
            return oldItem.time_epoch == newItem.time_epoch
        }

        override fun areContentsTheSame(oldItem: Hour, newItem: Hour): Boolean {
            return oldItem == newItem
        }
    }
}