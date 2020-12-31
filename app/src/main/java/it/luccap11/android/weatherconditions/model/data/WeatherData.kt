package it.luccap11.android.weatherconditions.model.data

/**
 * @author Luca Capitoli
 */
data class WeatherData(
    val city: String,
    val descr: String,
    val temp: Float,
    val date: String,
    val icon: String
) {

}