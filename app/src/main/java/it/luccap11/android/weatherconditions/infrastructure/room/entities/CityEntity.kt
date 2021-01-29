package it.luccap11.android.weatherconditions.infrastructure.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author Luca Capitoli
 * @since 29/jan/2021
 */
@Entity(tableName = "cities")
data class CityEntity (
    @PrimaryKey(autoGenerate=true) val uid: Int,
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "country_name") var country: String = "",
    @ColumnInfo(name = "population") var population: Int = 0
        )