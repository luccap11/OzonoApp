package it.luccap11.android.weatherconditions.model

import androidx.annotation.NonNull
import it.luccap11.android.weatherconditions.model.data.WeatherData

/**
 * @author Luca Capitoli
 * @since 31/dec/2020
 */
object SimpleWeatherDataCache {
    private var cachedWeatherData = mutableMapOf<String, List<WeatherData>>()

    fun getCachedWeatherData(@NonNull selectedCity: String): List<WeatherData>? {
        return cachedWeatherData[selectedCity]
    }

    fun addCachedWeatherData(@NonNull weatherData: List<WeatherData>) {
        cachedWeatherData[weatherData[0].city] = weatherData
    }

    @NonNull
    fun isWeatherDataInCache(@NonNull selectedCity: String): Boolean {
        return cachedWeatherData.containsKey(selectedCity)
    }
}