package it.luccap11.android.weatherconditions.infrastructure

import it.luccap11.android.weatherconditions.R
import it.luccap11.android.weatherconditions.OzonoAppl
import it.luccap11.android.weatherconditions.infrastructure.room.AppDatabase
import it.luccap11.android.weatherconditions.infrastructure.room.entities.CityEntityBuilder
import it.luccap11.android.weatherconditions.model.data.*
import it.luccap11.android.weatherconditions.utils.AppUtils
import it.luccap11.android.weatherconditions.utils.PreferencesManager
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
    private val resources = OzonoAppl.instance.resources
    private val numbOfResults = resources.getInteger(R.integer.num_of_cities_result)
    private val citiesDao = AppDatabase.getInstance().citiesDao()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + job

    fun fetchLocalCitiesData(userQuery: String, completion: (Resource<List<CityData>>) -> Unit) {
        val isCachedData = CitiesDataCache.isDataInCache(userQuery)
        if (isCachedData) {
            completion(Resource.Success(CitiesDataCache.getCachedCitiesData(userQuery)))
        } else {
            launch {
                val dbCities = citiesDao.findCitiesStartWith("$userQuery%", numbOfResults)

                if (dbCities.isNotEmpty() && dbCities.size >= numbOfResults) {
                    val cities = CityDataBuilder().cityDataBuilder(dbCities)
                    CitiesDataCache.addCachedCityData(userQuery, cities)
                    completion(Resource.Success(cities))
                } else {
                    completion(Resource.Error(""))
                }
            }
        }
    }

    suspend fun fetchRemoteCitiesData(userQuery: String): List<CityData> {
        return RemoteWCitiesDataSource.fetchBack4AppData(userQuery)
    }

    fun getLastCitySearched(completion: (CityData?) -> Unit) {
        val prefs = PreferencesManager()
        val lastLatitSearched = prefs.getLastSearchedCityLatit()
        val lastLongitSearched = prefs.getLastSearchedCityLongit()
        if (!lastLatitSearched.equals(AppUtils.NOT_SET.toFloat()) && !lastLongitSearched.equals(
                AppUtils.NOT_SET.toFloat())) {
            launch {
                val cityEntity = citiesDao.findCityByCoords(lastLatitSearched, lastLongitSearched)
                if (cityEntity == null) {
                    completion(null)
                } else {
                    val dbData = CityDataBuilder().cityDataBuilder(listOf(cityEntity))
                    completion(dbData[0])
                }
            }
        } else {
            completion(null)
        }
    }
}