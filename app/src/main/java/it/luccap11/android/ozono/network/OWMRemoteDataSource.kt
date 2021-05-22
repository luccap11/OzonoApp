package it.luccap11.android.ozono.network

import android.util.Log
import it.luccap11.android.ozono.model.data.WeatherData
import it.luccap11.android.ozono.repository.WeatherApiService
import it.luccap11.android.ozono.repository.retrofit
import it.luccap11.android.ozono.utils.AppUtils.TAG_LOG

/**
 * @author Luca Capitoli
 * @since 30/dec/2020
 */
object OWMRemoteDataSource: RemoteWDataSource {
    private val retrofitService : WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }

    override suspend fun fetchOWeatherMapData(selectedCity: String): List<WeatherData>? {
        try {

            return listOf(retrofitService.fetchRemoteWeatherData(selectedCity))
//            return emptyList()
        } catch (e: Exception) {
            Log.e(TAG_LOG, e.toString(), e)
        }
        return null
    }
}