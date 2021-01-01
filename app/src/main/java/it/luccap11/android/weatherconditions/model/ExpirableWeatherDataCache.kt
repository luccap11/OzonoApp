package it.luccap11.android.weatherconditions.model

import androidx.annotation.NonNull
import androidx.collection.LruCache
import it.luccap11.android.weatherconditions.model.data.WeatherData

/**
 * @author Luca Capitoli
 * @since 31/dec/2020
 */
object ExpirableWeatherDataCache {
    private val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
    private val cacheSize = maxMemory / 8
    private var cachedWeatherData = object : LruCache<String, List<WeatherData>>(cacheSize) {
        override fun sizeOf(key: String, weatherData: List<WeatherData>): Int {
            return weatherData.size
        }
    }

    fun getCachedWeatherData(@NonNull selectedCity: String): List<WeatherData>? {
        return cachedWeatherData[selectedCity]
    }

    fun addCachedWeatherData(@NonNull weatherData: List<WeatherData>) {
        cachedWeatherData.put(weatherData[0].city, weatherData)
    }

    @NonNull
    fun isWeatherDataInCache(@NonNull selectedCity: String): Boolean {
        val weatherData = cachedWeatherData[selectedCity]
        return if (weatherData.isNullOrEmpty()) {
            false
        } else {
            if (isItemExpired(weatherData[0].timeInMillis)) {
                cachedWeatherData.remove(selectedCity)
                false
            } else {
                true
            }
        }
    }

    private fun isItemExpired(weatherDataTime: Long): Boolean {
        val threeHoursInMillis = 60 * 60 * 1000 * 3
        val expiringTime = weatherDataTime + threeHoursInMillis - 1
        if (expiringTime < System.currentTimeMillis()) {
            return true
        }
        return false
    }
}