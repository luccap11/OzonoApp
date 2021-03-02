package it.luccap11.android.weatherconditions.infrastructure.room.daos

import androidx.annotation.NonNull
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
    @Query("SELECT * FROM cities WHERE name = :name AND country_name = :country")
    suspend fun getCity(@NonNull name: String, @NonNull country: String): CityEntity

    @Query("SELECT * FROM cities WHERE name LIKE :startName ORDER BY population DESC LIMIT :numbOfResult")
    suspend fun getCitiesStartWith(@NonNull startName: String, numbOfResult: Int): List<CityEntity>

    @Query("SELECT * FROM cities WHERE latitude = :latitude AND longitude = :longitude")
    suspend fun getCityByCoords(@NonNull latitude: Float, longitude: Float): CityEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCities(vararg city: CityEntity): List<Long>
}