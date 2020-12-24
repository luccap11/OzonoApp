package it.luccap11.android.weatherconditions.model

import it.luccap11.android.weatherconditions.model.data.WeatherData

/**
 * @author Luca Capitoli
 */
interface WeatherRepository {
    fun fetchWeatherData(selectedCity: String, completion: (Resource<List<WeatherData>>) -> Unit)
}