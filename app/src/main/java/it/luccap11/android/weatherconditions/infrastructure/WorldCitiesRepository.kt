package it.luccap11.android.weatherconditions.infrastructure

import it.luccap11.android.weatherconditions.R
import it.luccap11.android.weatherconditions.WeatherConditionApp
import it.luccap11.android.weatherconditions.infrastructure.room.AppDatabase
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
    private val resources = WeatherConditionApp.instance.resources
    private val numbOfResults = resources.getInteger(R.integer.num_of_cities_result)

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + job

    fun fetchLocalCitiesData(userQuery: String, completion: (Resource<List<CityData>>) -> Unit) {
        val isCachedData = CitiesDataCache.isDataInCache(userQuery)
        if (isCachedData) {
            completion(Resource.Success(CitiesDataCache.getCachedCitiesData(userQuery)))
        } else {
            launch {
                val dbCities = AppDatabase.getInstance().citiesDao().getCitiesStartWith("$userQuery%", numbOfResults)

                if (dbCities.isNotEmpty() && dbCities.size >= resources.getInteger(R.integer.num_of_cities_result)) {
                    val cities = CityDataBuilder().cityDataBuilder(dbCities)
                    CitiesDataCache.addCachedCityData(userQuery, cities)
                    completion(Resource.Success(cities))
                } else {
                    completion(Resource.Error(""))
                }
            }
        }
    }

    fun fetchRemoteCitiesData(userQuery: String, completion: (Resource<List<CityData>>) -> Unit) {
        RemoteWCitiesDataSource.fetchBack4AppData(userQuery) { remoteResponse ->
            when (remoteResponse) {
                is Resource.Success -> {
                    CitiesDataCache.deleteMultipleCachedCityData(userQuery.substring(0..userQuery.length - 2))
                    val citiesEntity = CityEntityBuilder().cityEntityBuilder(remoteResponse.data!!)
                    launch {
                        val db = AppDatabase.getInstance().citiesDao()
                        db.insertCities(*citiesEntity)
                        val dbCitiesEntity = db.getCitiesStartWith("$userQuery%", numbOfResults)
                        val dbData = CityDataBuilder().cityDataBuilder(dbCitiesEntity)
                        completion(Resource.Success(dbData))
                    }
                    completion(Resource.Loading())
                }

                is Resource.Error -> {
                    completion(Resource.Error(resources.getString(R.string.error_label)))
                }
            }
        }
    }
}