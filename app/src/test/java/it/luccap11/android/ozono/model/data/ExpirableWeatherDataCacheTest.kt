package it.luccap11.android.ozono.model.data

import it.luccap11.android.ozono.model.WeatherDataJsonResponseExample
import org.hamcrest.core.Is.`is`
import org.json.JSONObject
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * @author Luca Capitoli
 * @since 02/jan/2021
 */
@RunWith(JUnit4::class)
class ExpirableWeatherDataCacheTest {
    @Test
    fun getCachedWeatherData_EmptyCache() {
        val selectedCity = "London"
        val weatherData = ExpirableWeatherDataCache.getCachedWeatherData(selectedCity)
        assertEquals(null, weatherData)
        assertFalse(ExpirableWeatherDataCache.isWeatherDataInCache(selectedCity))
    }

    @Test
    fun getCachedWeatherData_FilledCache() {
        val selectedCity = "London"
        val weatherData = getWeatherData(System.currentTimeMillis())

        ExpirableWeatherDataCache.addCachedWeatherData(weatherData)
        val weatherDataCached = ExpirableWeatherDataCache.getCachedWeatherData(selectedCity)
        assertThat(weatherDataCached!![0].city, `is`(selectedCity))
        assertTrue(ExpirableWeatherDataCache.isWeatherDataInCache(selectedCity))
    }

    @Test
    fun isWeatherDataInCache_Empty() {
        assertFalse(ExpirableWeatherDataCache.isWeatherDataInCache("London"))
    }

    @Test
    fun isWeatherDataInCache_itemIsNotExpired() {
        val weatherData = getWeatherData(System.currentTimeMillis())
        ExpirableWeatherDataCache.addCachedWeatherData(weatherData)
        assertTrue(ExpirableWeatherDataCache.isWeatherDataInCache("London"))
    }

    @Test
    fun isWeatherDataInCache_ItemIsExpired() {
        val weatherData = getWeatherData(0)
        ExpirableWeatherDataCache.addCachedWeatherData(weatherData)
        assertFalse(ExpirableWeatherDataCache.isWeatherDataInCache("London"))
    }

    private fun getWeatherData(timeInMillis: Long): List<WeatherData> {
        val jsonResponse = WeatherDataJsonResponseExample.fetchJson(timeInMillis / 1000)
        val jsonObject = JSONObject(jsonResponse)
        return WeatherDataParser.getWeatherLiveData(jsonObject)
    }
}