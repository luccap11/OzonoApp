package it.luccap11.android.weatherconditions.infrastructure.room.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import it.luccap11.android.weatherconditions.infrastructure.room.entities.CityEntity

/**
 * @author Luca Capitoli
 * @since 29/jan/2021
 */
@Dao
interface CitiesDao {

    @Query("SELECT * FROM cities WHERE name LIKE :startName ORDER BY population DESC LIMIT :numbOfResult")
    suspend fun findCitiesStartWith(startName: String, numbOfResult: Int): List<CityEntity>

    @Query("SELECT * FROM cities WHERE latitude = :latitude AND longitude = :longitude")
    fun findCityByCoords(latitude: Float, longitude: Float): CityEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCities(vararg city: CityEntity): List<Long>
}