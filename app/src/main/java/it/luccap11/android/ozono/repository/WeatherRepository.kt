package it.luccap11.android.ozono.repository

import it.luccap11.android.ozono.infrastructure.Resource
import it.luccap11.android.ozono.model.data.WeatherData

/**
 * @author Luca Capitoli
 */
interface WeatherRepository {
    fun fetchWeatherData(selectedCity: String, completion: (Resource<List<WeatherData>>) -> Unit)
}