package it.luccap11.android.ozono.network

import android.util.Log
import it.luccap11.android.ozono.model.data.WeatherData
import it.luccap11.android.ozono.utils.AppUtils.TAG_LOG

/**
 * @author Luca Capitoli
 * @since 30/dec/2020
 */
object OWMRemoteDataSource : RemoteWeatherDataSource {
    private val retrofitService: WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }

    override suspend fun fetchOWeatherMapData(selectedCity: String): WeatherData? {
        try {
            return retrofitService.fetchRemoteWeatherData(selectedCity)
        } catch (e: Exception) {
            Log.e(TAG_LOG, e.toString(), e)
        }
        return null
    }
}