package it.luccap11.android.weatherconditions.infrastructure

import it.luccap11.android.weatherconditions.R
import it.luccap11.android.weatherconditions.OzonoAppl
import it.luccap11.android.weatherconditions.model.data.ExpirableWeatherDataCache
import it.luccap11.android.weatherconditions.model.data.WeatherData
import it.luccap11.android.weatherconditions.model.data.WeatherDataParser

/**
 * @author Luca Capitoli
 */
class OWeatherMapRepository: WeatherRepository {

    override fun fetchWeatherData(selectedCity: String, completion: (Resource<List<WeatherData>>) -> Unit) {
        val isCachedData = ExpirableWeatherDataCache.isWeatherDataInCache(selectedCity)
        if (isCachedData) {
            completion(Resource.Success(ExpirableWeatherDataCache.getCachedWeatherData(selectedCity)!!))
        } else {
            RemoteWeatherDataSource.fetchOWeatherMapData(selectedCity) { remoteResponse ->
                when (remoteResponse) {
                    is Resource.Success -> {
                        val result = WeatherDataParser.getWeatherLiveData(remoteResponse.data!!)
                        ExpirableWeatherDataCache.addCachedWeatherData(result)
                        completion(Resource.Success(result))
                    }

                    is Resource.Error -> {
                        completion(Resource.Error(OzonoAppl.instance.resources.getString(R.string.error_label)))
                    }
                }
            }
        }
    }
}