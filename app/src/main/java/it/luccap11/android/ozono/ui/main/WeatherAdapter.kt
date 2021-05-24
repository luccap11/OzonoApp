package it.luccap11.android.ozono.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import it.luccap11.android.ozono.R
import it.luccap11.android.ozono.model.data.ListData
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * @author Luca Capitoli
 */
class WeatherAdapter(private val dataSet: List<ListData>) :
    RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val date: TextView = view.findViewById(R.id.day)
        val descr: TextView = view.findViewById(R.id.descr)
        val temp: TextView = view.findViewById(R.id.temperature)
        val img: ImageView = view.findViewById(R.id.weatherImage)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.weather_row_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.date.text = dateFormatter(dataSet[position].timeInSecs * 1000)
        viewHolder.descr.text = dataSet[position].weather[0].descr
        viewHolder.temp.text = String.format("%d °C", dataSet[position].main.temp.toInt())
        val imgUrl = String.format(
            "https://openweathermap.org/img/wn/%s@2x.png",
            dataSet[position].weather[0].icon
        )
        Glide.with(viewHolder.img.context).load(imgUrl)
            .placeholder(R.drawable.ic_baseline_image_not_supported_24)
            .circleCrop().into(viewHolder.img)

    }

    override fun getItemCount() = dataSet.size

    private fun dateFormatter(timeInMillis: Long): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM");
        val instant = Instant.ofEpochMilli(timeInMillis)
        val date = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        return formatter.format(date)
    }

}