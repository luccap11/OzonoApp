package it.luccap11.android.ozono.infrastructure.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import it.luccap11.android.ozono.model.data.Hits

/**
 * @author Luca Capitoli
 * @since 29/jan/2021
 */
@Entity(tableName = "cities", primaryKeys = ["name","country_name","latitude", "longitude"])
data class CityEntity (
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "country_name") var country: String = "",
    @ColumnInfo(name = "population") var population: Int = 0,
    @ColumnInfo(name = "adminCode") var adminCode: String = "",
    @ColumnInfo(name = "latitude") val latitude: Float,
    @ColumnInfo(name = "longitude") val longitude: Float,
    @ColumnInfo(name = "insert_date") var insertDate: Long = 0
)

 class CityEntityBuilder {
    fun cityEntityBuilder(citiesData: List<Hits>): Array<CityEntity> {
        val result = arrayListOf<CityEntity>()
        citiesData.forEach { cityData ->
            val newCity = CityEntity(cityData.localeNames.default[0], cityData.country.name, -1,
                cityData.administrative[0], cityData.geoloc.lat, cityData.geoloc.lng,
                System.currentTimeMillis())
            result.add(newCity)
        }
        return result.toTypedArray()
    }
}