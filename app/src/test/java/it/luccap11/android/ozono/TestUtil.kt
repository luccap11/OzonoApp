package it.luccap11.android.ozono

import it.luccap11.android.ozono.infrastructure.room.entities.CityEntity
import it.luccap11.android.ozono.model.data.*

class TestUtil {

    fun mockWeatherData(timeInMillis: Long): ListData {
        return ListData(
            timeInMillis,
            listOf(Weather("icon")),
            Main(23.4f)
        )
    }

    fun mockCityData(): CityData {
        return CityData(
            Country("UK"),
            listOf("region"),
            Geoloc(12.3456f, 12.3456f),
            LocaleNames(listOf("London"))
        )
    }

    fun mockCityEntity(insertTimeInMillis: Long): CityEntity {
        return CityEntity(
            "Lisbona",
            "Portogallo",
            0,
            "admin code",
            12.3456f,
            12.3456f,
            insertTimeInMillis
        )
    }
}