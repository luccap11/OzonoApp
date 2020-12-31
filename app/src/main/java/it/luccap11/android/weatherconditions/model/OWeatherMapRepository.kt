package it.luccap11.android.weatherconditions.model

import it.luccap11.android.weatherconditions.R
import it.luccap11.android.weatherconditions.WeatherConditionApp
import it.luccap11.android.weatherconditions.model.data.WeatherData
import it.luccap11.android.weatherconditions.model.data.WeatherDataParser

/**
 * @author Luca Capitoli
 */
class OWeatherMapRepository: WeatherRepository {

    override fun fetchWeatherData(selectedCity: String, completion: (Resource<List<WeatherData>>) -> Unit) {
        val isCachedData = SimpleWeatherDataCache.isWeatherDataInCache(selectedCity)
        if (isCachedData) {
            completion(Resource.Success(SimpleWeatherDataCache.getCachedWeatherData(selectedCity)!!))
        } else {
            RemoteWeatherDataSource.fetchOWeatherMapData(selectedCity) { remoteResponse ->
                when (remoteResponse) {
                    is Resource.Success -> {
                        val result = WeatherDataParser.getWeatherLiveData(remoteResponse.data!!)
                        SimpleWeatherDataCache.addCachedWeatherData(result)
                        completion(Resource.Success(result))
                    }

                    is Resource.Error -> {
                        completion(Resource.Error(WeatherConditionApp.instance.resources.getString(R.string.error_label)))
                    }
                }
            }
        }
    }
}