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
    fun getCity(@NonNull name: String, @NonNull country: String): CityEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCity(vararg city: CityEntity)
}