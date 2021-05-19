package it.luccap11.android.ozono.model.data

/**
 * @author Luca Capitoli
 */
data class WeatherData(
    val city: String,
    val descr: String,
    val temp: Float,
    val date: String,
    val icon: String,
    val timeInMillis: Long
)