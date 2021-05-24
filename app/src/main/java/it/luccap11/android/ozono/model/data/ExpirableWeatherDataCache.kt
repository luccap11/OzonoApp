package it.luccap11.android.ozono.model.data

import androidx.collection.LruCache

/**
 * @author Luca Capitoli
 * @since 31/dec/2020
 */
object ExpirableWeatherDataCache {
    private val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
    private val cacheSize = maxMemory / 8
    private var cachedWeatherData = object : LruCache<String, WeatherData>(cacheSize) {
        override fun sizeOf(key: String, weatherData: WeatherData): Int {
            return weatherData.list.size
        }
    }

    fun getCachedWeatherData(selectedCity: String): WeatherData? {
        return cachedWeatherData[selectedCity]
    }

    fun addCachedWeatherData(weatherData: WeatherData) {
        cachedWeatherData.put(weatherData.city.name, weatherData)
    }

    fun isWeatherDataInCache(selectedCity: String): Boolean {
        val weatherData = cachedWeatherData[selectedCity]
        return if (weatherData == null) {
            false
        } else {
            if (isItemExpired(weatherData.list[0].timeInSecs)) {
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