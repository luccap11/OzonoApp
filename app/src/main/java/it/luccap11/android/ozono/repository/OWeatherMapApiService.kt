package it.luccap11.android.ozono.repository

import it.luccap11.android.ozono.model.data.ExpirableWeatherDataCache
import it.luccap11.android.ozono.model.data.WeatherData
import it.luccap11.android.ozono.network.OWMRemoteDataSource

/**
 * @author Luca Capitoli
 */
class OWeatherMapApiService(private val remoteDataSource: OWMRemoteDataSource) {

    suspend fun fetchWeatherDataByCityName(selectedCity: String): List<WeatherData>? {
        val isCachedData = ExpirableWeatherDataCache.isWeatherDataInCache(selectedCity)
        if (isCachedData) {
            return ExpirableWeatherDataCache.getCachedWeatherData(selectedCity)!!
        } else {
            return remoteDataSource.fetchOWeatherMapData(selectedCity)
//            RemoteWeatherDataSource.fetchOWeatherMapData(selectedCity) { remoteResponse ->
//                when (remoteResponse) {
//                    is Resource.Success -> {
//                        val result = WeatherDataParser.getWeatherLiveData(remoteResponse.data!!)
//                        ExpirableWeatherDataCache.addCachedWeatherData(result)
//                        completion(Resource.Success(result))
//                    }
//
//                    is Resource.Error -> {
//                        completion(Resource.Error(OzonoAppl.instance.resources.getString(R.string.error_label)))
//                    }
//                }
//            }
        }
    }
}