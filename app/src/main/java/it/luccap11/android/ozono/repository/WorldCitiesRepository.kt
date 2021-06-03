package it.luccap11.android.ozono.repository

import it.luccap11.android.ozono.infrastructure.room.daos.CitiesDao
import it.luccap11.android.ozono.infrastructure.room.entities.CityEntityBuilder
import it.luccap11.android.ozono.model.data.*
import it.luccap11.android.ozono.network.AlgoliaCitiesRemoteDataSource
import it.luccap11.android.ozono.utils.AppUtils
import it.luccap11.android.ozono.utils.PreferencesManager

/**
 * @author Luca Capitoli
 * @since 13/jan/2021
 */
class WorldCitiesRepository(
    private val cache: CitiesDataCache, private val citiesDao: CitiesDao,
    private val remoteDataSource: AlgoliaCitiesRemoteDataSource,
    private val prefs: PreferencesManager
) {

    suspend fun fetchLocalCitiesData(userQuery: String, numbOfResults: Int): List<CityData> {
        val cacheData = cache.getCachedCitiesData(userQuery)
        return if (cacheData.isNotEmpty()) {
            cacheData
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

    suspend fun fetchRemoteCitiesData(userQuery: String, numbOfResults: Int): List<CityData>? {
        val remoteCities = remoteDataSource.fetchAlgoliaData(userQuery)
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
        val lastLatitSearched = prefs.getLastSearchedCityLatit()
        val lastLongitSearched = prefs.getLastSearchedCityLongit()
        return if (!lastLatitSearched.equals(AppUtils.NOT_SET.toFloat()) && !lastLongitSearched.equals(
                AppUtils.NOT_SET.toFloat()
            )
        ) {
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