package it.luccap11.android.ozono.repository

import it.luccap11.android.ozono.model.data.ExpirableWeatherDataCache
import it.luccap11.android.ozono.model.data.ListData
import it.luccap11.android.ozono.network.OWMRemoteDataSource

/**
 * @author Luca Capitoli
 */
class WeatherDataRepository(private val remoteDataSource: OWMRemoteDataSource) {

    suspend fun fetchWeatherDataByCityName(selectedCity: String): List<ListData>? {
        val isCachedData = ExpirableWeatherDataCache.isWeatherDataInCache(selectedCity)
        return if (isCachedData) {
            ExpirableWeatherDataCache.getCachedWeatherData(selectedCity)!!.list
        } else {
            remoteDataSource.fetchOWeatherMapData(selectedCity)?.list
        }
    }
}