package it.luccap11.android.weatherconditions.infrastructure

import it.luccap11.android.weatherconditions.R
import it.luccap11.android.weatherconditions.OzonoAppl
import it.luccap11.android.weatherconditions.infrastructure.room.daos.CitiesDao
import it.luccap11.android.weatherconditions.infrastructure.room.entities.CityEntityBuilder
import it.luccap11.android.weatherconditions.model.data.*
import it.luccap11.android.weatherconditions.utils.AppUtils
import it.luccap11.android.weatherconditions.utils.PreferencesManager

/**
 * @author Luca Capitoli
 * @since 13/jan/2021
 */
class WorldCitiesRepository(private val cache: CitiesDataCache, private val citiesDao: CitiesDao, private val remoteDataSource: RemoteWCitiesDataSource) {
    private val resources = OzonoAppl.instance.resources
    private val numbOfResults = resources.getInteger(R.integer.num_of_cities_result)

    suspend fun fetchLocalCitiesData(userQuery: String): List<CityData> {
        val isCachedData = cache.isDataInCache(userQuery)
        return if (isCachedData) {
            cache.getCachedCitiesData(userQuery)
        } else {
            val dbCities = citiesDao.findCitiesStartWith("$userQuery%", numbOfResults)

            if (dbCities.isNotEmpty() && dbCities.size >= numbOfResults) {
                val cities = CityDataBuilder().cityDataBuilder(dbCities)
                cache.addCachedCityData(userQuery, cities)
                cities
            } else {
                emptyList()
            }
        }
    }

    suspend fun fetchRemoteCitiesData(userQuery: String): List<CityData>? {
        val remoteCities = remoteDataSource.fetchBack4AppData(userQuery)
        if (!remoteCities.isNullOrEmpty()) {
            cache.deleteMultipleCachedCityData(userQuery.substring(0..userQuery.length - 2))
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