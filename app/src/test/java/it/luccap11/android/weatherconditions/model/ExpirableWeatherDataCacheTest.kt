package it.luccap11.android.weatherconditions.model

import it.luccap11.android.weatherconditions.model.data.WeatherData
import it.luccap11.android.weatherconditions.model.data.WeatherDataParser
import org.json.JSONObject
import org.junit.Assert
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
        Assert.assertEquals(null, weatherData)
        Assert.assertEquals(false, ExpirableWeatherDataCache.isWeatherDataInCache(selectedCity))
    }

    @Test
    fun getCachedWeatherData_FilledCache() {
        val selectedCity = "London"
        val weatherData = getWeatherData(System.currentTimeMillis())

        ExpirableWeatherDataCache.addCachedWeatherData(weatherData)
        val weatherDataCached = ExpirableWeatherDataCache.getCachedWeatherData(selectedCity)
        Assert.assertEquals(selectedCity, weatherDataCached!![0].city)
        Assert.assertEquals(true, ExpirableWeatherDataCache.isWeatherDataInCache(selectedCity))
    }

    @Test
    fun isWeatherDataInCache_Empty() {
        Assert.assertEquals(false, ExpirableWeatherDataCache.isWeatherDataInCache("London"))
    }

    @Test
    fun isWeatherDataInCache_itemIsNotExpired() {
        val weatherData = getWeatherData(System.currentTimeMillis())
        ExpirableWeatherDataCache.addCachedWeatherData(weatherData)
        Assert.assertEquals(true, ExpirableWeatherDataCache.isWeatherDataInCache("London"))
    }

    @Test
    fun isWeatherDataInCache_ItemIsExpired() {
        val weatherData = getWeatherData(0)
        ExpirableWeatherDataCache.addCachedWeatherData(weatherData)
        Assert.assertEquals(false, ExpirableWeatherDataCache.isWeatherDataInCache("London"))
    }

    private fun getWeatherData(timeInMillis: Long): List<WeatherData> {
        val jsonResponse = JsonResponseExample.fetchJson(timeInMillis / 1000)
        val jsonObject = JSONObject(jsonResponse)
        return WeatherDataParser.getWeatherLiveData(jsonObject)
    }
}