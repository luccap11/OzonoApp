package it.luccap11.android.ozono.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import it.luccap11.android.ozono.R
import it.luccap11.android.ozono.databinding.WeatherRowItemBinding
import it.luccap11.android.ozono.model.data.ListData
import it.luccap11.android.ozono.model.data.WeatherData
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * @author Luca Capitoli
 */
class WeatherAdapter : ListAdapter<ListData, WeatherAdapter.ViewHolder>(DiffCallback) {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(private var binding: WeatherRowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(weatherData: ListData) {
            binding.todayLabel.visibility = if (layoutPosition == 0) View.VISIBLE else View.GONE

            binding.date = weatherData
            binding.weatherData = weatherData.weather[0]
            binding.temp = weatherData.main
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        return ViewHolder(WeatherRowItemBinding.inflate(inflater, viewGroup, false))
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val weatherData = getItem(position)
        viewHolder.bind(weatherData)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<ListData>() {

        override fun areItemsTheSame(oldItem: ListData, newItem: ListData): Boolean {
            return oldItem.timeInSecs == newItem.timeInSecs
        }

        override fun areContentsTheSame(oldItem: ListData, newItem: ListData): Boolean {
            return oldItem.timeInSecs == newItem.timeInSecs
        }
    }

}