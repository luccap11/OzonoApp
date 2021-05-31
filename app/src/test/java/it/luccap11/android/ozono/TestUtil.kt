package it.luccap11.android.ozono

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
}