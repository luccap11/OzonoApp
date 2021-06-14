package it.luccap11.android.ozono.ui.main

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import it.luccap11.android.ozono.R
import it.luccap11.android.ozono.model.data.CityData
import it.luccap11.android.ozono.model.data.ListData
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@BindingAdapter("dayText")
fun bindDay(textView: TextView, timeInSecs: Long) {
    val timeInMillis = timeInSecs * 1000
    textView.text = dateFormatter(timeInMillis, "dd")
}

@BindingAdapter("monthText")
fun bindMonth(textView: TextView, timeInSecs: Long) {
    val timeInMillis = timeInSecs * 1000
    textView.text = dateFormatter(timeInMillis, "/MM")
}

@BindingAdapter("dayOfTheWeek")
fun bindDayOfTheWeek(textView: TextView, timeInSecs: Long) {
    val timeInMillis = timeInSecs * 1000
    textView.text = dateFormatter(timeInMillis, "EEEE")
}

@BindingAdapter("weatherImageUrl")
fun bindImage(imageView: ImageView, url: String?) {
    url.let {
        val imgUrl = String.format(
            "https://openweathermap.org/img/wn/%s@2x.png",
            url
        )
        Glide.with(imageView.context).load(imgUrl)
            .placeholder(R.drawable.loading_animation)
            .error(R.drawable.ic_baseline_image_not_supported_24)
            .circleCrop().into(imageView)
    }
}

@BindingAdapter("temp")
fun bindTemperature(textView: TextView, temp: Float) {
    textView.text = String.format("%d °C", temp.toInt())
}

@BindingAdapter("listWeatherData")
fun bindWeatherRecyclerView(recyclerView: RecyclerView, data: List<ListData>?) {
    val adapter = recyclerView.adapter as WeatherAdapter
    adapter.submitList(data)
    adapter.notifyDataSetChanged()
}

@BindingAdapter("listCitiesData")
fun bindCitiesRecyclerView(recyclerView: RecyclerView, data: List<CityData>?) {
    val adapter = recyclerView.adapter as CitiesAdapter
    adapter.submitList(data)
}

@BindingAdapter("cityText")
fun bindCityName(textView: TextView, cityData: CityData) {
    textView.text = String.format("%s, %s, %s", cityData.localeNames.cityNames[0],
        cityData.region[0], cityData.country.name)
}

private fun dateFormatter(timeInMillis: Long, pattern: String): String {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    val instant = Instant.ofEpochMilli(timeInMillis)
    val date = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
    return formatter.format(date)
}