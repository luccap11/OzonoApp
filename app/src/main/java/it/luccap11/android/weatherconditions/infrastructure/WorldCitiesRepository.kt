package it.luccap11.android.weatherconditions.infrastructure

import it.luccap11.android.weatherconditions.R
import it.luccap11.android.weatherconditions.WeatherConditionApp
import it.luccap11.android.weatherconditions.model.data.*

/**
 * @author Luca Capitoli
 * @since 13/jan/2021
 */
class WorldCitiesRepository {

    fun fetchCitiesData(userQuery: String, completion: (Resource<List<CityData>>) -> Unit) {
        val isCachedData = CitiesDataCache.isDataInCache(userQuery)
        if (isCachedData) {
            completion(Resource.Success(CitiesDataCache.getCachedCitiesData(userQuery)))
        } else if (false) {
            //check in db...
        } else {
            RemoteWCitiesDataSource.fetchBack4AppData(userQuery) { remoteResponse ->
                when (remoteResponse) {
                    is Resource.Success -> {
                        CitiesDataCache.addCachedCityData(userQuery, remoteResponse.data!!)
                        completion(Resource.Success(remoteResponse.data))
                    }

                    is Resource.Error -> {
                        completion(Resource.Error(WeatherConditionApp.instance.resources.getString(R.string.error_label)))
                    }
                }
            }
        }
    }
}