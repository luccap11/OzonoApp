package it.luccap11.android.weatherconditions.infrastructure

import it.luccap11.android.weatherconditions.R
import it.luccap11.android.weatherconditions.WeatherConditionApp
import it.luccap11.android.weatherconditions.infrastructure.room.AppDatabase
import it.luccap11.android.weatherconditions.infrastructure.room.entities.CityEntity
import it.luccap11.android.weatherconditions.infrastructure.room.entities.CityEntityBuilder
import it.luccap11.android.weatherconditions.model.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * @author Luca Capitoli
 * @since 13/jan/2021
 */
class WorldCitiesRepository: CoroutineScope {
    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + job

    fun fetchLocalCitiesData(userQuery: String, completion: (Resource<List<CityData>>) -> Unit) {
        val isCachedData = CitiesDataCache.isDataInCache(userQuery)
        if (isCachedData) {
            completion(Resource.Success(CitiesDataCache.getCachedCitiesData(userQuery)))
        } else {
            var dbCities = emptyList<CityEntity>()
            launch {
                dbCities = AppDatabase.getInstance().citiesDao().getCitiesStartWith(userQuery+"%")
            }

            if (dbCities.isNotEmpty()) {
                val cities = CityDataBuilder().cityDataBuilder(dbCities)
                CitiesDataCache.addCachedCityData(userQuery, cities)
                completion(Resource.Success(cities))
            } else {
               completion(Resource.Error(""))
            }
        }
    }

    fun fetchRemoteCitiesData(userQuery: String, completion: (Resource<List<CityData>>) -> Unit) {
        RemoteWCitiesDataSource.fetchBack4AppData(userQuery) { remoteResponse ->
            when (remoteResponse) {
                is Resource.Success -> {
                    CitiesDataCache.addCachedCityData(userQuery, remoteResponse.data!!)
                    val citiesEntity = CityEntityBuilder().cityEntityBuilder(remoteResponse.data)
                    launch {
                        AppDatabase.getInstance().citiesDao().insertCities(*citiesEntity)
                    }
                    completion(Resource.Success(remoteResponse.data))
                }

                is Resource.Error -> {
                    completion(Resource.Error(WeatherConditionApp.instance.resources.getString(
                        R.string.error_label
                    )))
                }
            }
        }
    }
}