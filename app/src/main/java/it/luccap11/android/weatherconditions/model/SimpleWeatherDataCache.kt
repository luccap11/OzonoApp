package it.luccap11.android.weatherconditions.model

import it.luccap11.android.weatherconditions.model.data.WeatherData

/**
 * @author Luca Capitoli
 */
object SimpleWeatherDataCache {
    var cachedWeatherData = mutableMapOf<String, List<WeatherData>>()

}