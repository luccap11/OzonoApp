package it.luccap11.android.weatherconditions.model.data

import androidx.annotation.NonNull
import androidx.collection.LruCache

/**
 * @author Luca Capitoli
 * @since 11/jan/2021
 */
object CitiesDataCache {
    private val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
    private val cacheSize = maxMemory / 8
    private var cachedCitiesData = object : LruCache<String, List<CityData>>(cacheSize) {
        override fun sizeOf(key: String, weatherData: List<CityData>): Int {
            return weatherData.size
        }
    }

    @NonNull
    fun getCachedCitiesData(@NonNull queryKey: String): List<CityData> {
        val cacheData = cachedCitiesData[queryKey]
        return cacheData?.filter { it -> it.name.startsWith(queryKey ,true) } ?: emptyList()
    }

    fun addCachedCityData(@NonNull queryKey: String, @NonNull cityData: CityData) {
        val cities = getCachedCitiesData(queryKey)
        cachedCitiesData.put(queryKey, cities.plus(cityData))
    }

    @NonNull
    fun isWeatherDataInCache(@NonNull queryKey: String): Boolean {
        return getCachedCitiesData(queryKey).isNotEmpty()
    }
}