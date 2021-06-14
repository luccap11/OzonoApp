package it.luccap11.android.ozono.model.data

import it.luccap11.android.ozono.infrastructure.room.entities.CityEntity
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertNotEquals
import org.junit.Test

/**
 * @author Luca Capitoli
 */
class CityDataTest {

    @Test
    fun testAllFields() {
        val cityData = AlgoliaModel(
            listOf(
                CityData(
                    Country("Italy"),
                    listOf("Friuli Venezia Giulia"),
                    Geoloc(10.1234f, 10.1234f),
                    LocaleNames(listOf("Udine"))
                )
            )
        )
        assertNotEquals(null, cityData)
        assertThat(cityData.list[0].country.name, `is`("Italy"))
        assertThat(cityData.list[0].region[0], `is`("Friuli Venezia Giulia"))
        assertThat(cityData.list[0].geoloc.lat, `is`(10.1234f))
        assertThat(cityData.list[0].geoloc.lng, `is`(10.1234f))
        assertThat(cityData.list[0].localeNames.cityNames[0], `is`("Udine"))
    }

    @Test
    fun testCityDataBuilder() {
        val cityEntity = CityEntity("Rome", "Italy", 100, "xxx", 12.12345f, 12.34567f)
        val cityEntity2 = CityEntity("Madrid", "Spain", 100, "xxx", 12.12345f, 12.34567f)

        val cities = CityDataBuilder().cityDataBuilder(listOf(cityEntity, cityEntity2))
        assertNotEquals(null, cities)
        checkCityData(cities)
    }

    private fun checkCityData(worldCitiesData: List<CityData>) {
        assertNotEquals(null, worldCitiesData)
        assertThat(worldCitiesData.size, `is`(2))

        assertThat(worldCitiesData[0].localeNames.cityNames[0], `is`("Rome"))
        assertThat(worldCitiesData[0].country.name, `is`("Italy"))
        assertThat(worldCitiesData[0].region[0], `is`("xxx"))
        assertThat(worldCitiesData[0].geoloc.lat, `is`(12.12345f))
        assertThat(worldCitiesData[0].geoloc.lng, `is`(12.34567f))

        assertThat(worldCitiesData[1].localeNames.cityNames[0], `is`("Madrid"))
        assertThat(worldCitiesData[1].country.name, `is`("Spain"))
        assertThat(worldCitiesData[1].region[0], `is`("xxx"))
        assertThat(worldCitiesData[1].geoloc.lat, `is`(12.12345f))
        assertThat(worldCitiesData[1].geoloc.lng, `is`(12.34567f))
    }

}