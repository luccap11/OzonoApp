package it.luccap11.android.weatherconditions.infrastructure

import it.luccap11.android.weatherconditions.R
import it.luccap11.android.weatherconditions.OzonoAppl
import it.luccap11.android.weatherconditions.infrastructure.room.AppDatabase
import it.luccap11.android.weatherconditions.infrastructure.room.entities.CityEntityBuilder
import it.luccap11.android.weatherconditions.model.data.*
import it.luccap11.android.weatherconditions.utils.AppUtils
import it.luccap11.android.weatherconditions.utils.PreferencesManager

/**
 * @author Luca Capitoli
 * @since 13/jan/2021
 */
class WorldCitiesRepository {
    private val resources = OzonoAppl.instance.resources
    private val numbOfResults = resources.getInteger(R.integer.num_of_cities_result)
    private val citiesDao = AppDatabase.getInstance().citiesDao()

    suspend fun fetchLocalCitiesData(userQuery: String): List<CityData> {
        val isCachedData = CitiesDataCache.isDataInCache(userQuery)
        return if (isCachedData) {
            CitiesDataCache.getCachedCitiesData(userQuery)
        } else {
            val dbCities = citiesDao.findCitiesStartWith("$userQuery%", numbOfResults)

            if (dbCities.isNotEmpty() && dbCities.size >= numbOfResults) {
                val cities = CityDataBuilder().cityDataBuilder(dbCities)
                CitiesDataCache.addCachedCityData(userQuery, cities)
                cities
            } else {
                emptyList()
            }
        }
    }

    suspend fun fetchRemoteCitiesData(userQuery: String): List<CityData>? {
        val remoteCities = RemoteWCitiesDataSource().fetchBack4AppData(userQuery)
        if (!remoteCities.isNullOrEmpty()) {
            CitiesDataCache.deleteMultipleCachedCityData(userQuery.substring(0..userQuery.length - 2))
            val citiesEntity = CityEntityBuilder().cityEntityBuilder(remoteCities)
            citiesDao.insertCities(*citiesEntity)
            val dbCitiesEntity = citiesDao.findCitiesStartWith("$userQuery%", numbOfResults)
            return CityDataBuilder().cityDataBuilder(dbCitiesEntity)
        }
        return remoteCities
    }

    suspend fun getLastCitySearched(): CityData? {
        val prefs = PreferencesManager()
        val lastLatitSearched = prefs.getLastSearchedCityLatit()
        val lastLongitSearched = prefs.getLastSearchedCityLongit()
        return if (!lastLatitSearched.equals(AppUtils.NOT_SET.toFloat()) && !lastLongitSearched.equals(AppUtils.NOT_SET.toFloat())) {
            val cityEntity = citiesDao.findCityByCoords(lastLatitSearched, lastLongitSearched)
            if (cityEntity == null) {
                null
            } else {
                val dbData = CityDataBuilder().cityDataBuilder(listOf(cityEntity))
                dbData[0]
            }
        } else {
            null
        }
    }
}