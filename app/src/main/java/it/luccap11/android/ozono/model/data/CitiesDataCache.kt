package it.luccap11.android.ozono.model.data

import androidx.collection.LruCache

/**
 * @author Luca Capitoli
 * @since 11/jan/2021
 */
object CitiesDataCache {
    private val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
    private val cacheSize = maxMemory / 8
    private var cachedCitiesData = object : LruCache<String, List<Hits>>(cacheSize) {
        override fun sizeOf(key: String, weatherData: List<Hits>): Int {
            return weatherData.size
        }
    }

    fun getCachedCitiesData(queryKey: String): List<Hits> {
        val cacheData = cachedCitiesData[queryKey]
        return cacheData?: emptyList()
    }

    fun addCachedCityData(queryKey: String, citiesData: List<Hits>) {
        val cities = getCachedCitiesData(queryKey)
        cachedCitiesData.put(queryKey, cities.plus(citiesData))
    }

    /**
     * Delete every key starts with @param key
     */
    fun deleteMultipleCachedCityData(key: String) {
        for (i in key.indices.reversed()) {
            cachedCitiesData.remove(key.substring(0..i))
        }
    }

    fun isDataInCache(queryKey: String): Boolean {
        return getCachedCitiesData(queryKey).isNotEmpty()
    }
}