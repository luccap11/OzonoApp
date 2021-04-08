package it.luccap11.android.weatherconditions.model.data

import it.luccap11.android.weatherconditions.infrastructure.room.entities.CityEntity
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert
import org.junit.Test

/**
 * @author Luca Capitoli
 */
class CityDataTest {

    @Test
    fun testLocation() {
        val location = Location(40.75123f, 16.54321f)
        Assert.assertNotEquals(null, location)
        Assert.assertThat(location.latitude, `is`(40.75123f))
        Assert.assertThat(location.longitude, `is`(16.54321f))
    }

    @Test
    fun testCountry() {
        val country = Country("Italy")
        Assert.assertNotEquals(null, country)
        Assert.assertThat(country.name, `is`("Italy"))
    }

    @Test
    fun testCity() {
        val cityData = CityData("Rome", Country("Italy"), 100, "xxx", Location(12.12345f, 12.34567f))
        Assert.assertNotEquals(null, cityData)
        Assert.assertThat(cityData.name, `is`("Rome"))
        Assert.assertThat(cityData.country.name, `is`("Italy"))
        Assert.assertThat(cityData.population, `is`(100))
        Assert.assertThat(cityData.adminCode, `is`("xxx"))
        Assert.assertThat(cityData.location.latitude, `is`(12.12345f))
        Assert.assertThat(cityData.location.longitude, `is`(12.34567f))
    }

    @Test
    fun testWorldCities() {
        val cityData = CityData("Rome", Country("Italy"), 100, "xxx", Location(12.12345f, 12.34567f))
        val cityData2 = CityData("Madrid", Country("Spain"), 100, "xxx", Location(12.12345f, 12.34567f))
        val worldCitiesData = WorldCitiesData(listOf(cityData, cityData2))

        Assert.assertNotEquals(null, worldCitiesData)
        checkCityData(worldCitiesData.cities)
    }

    @Test
    fun testCityDataBuilder() {
        val cityEntity = CityEntity("Rome", "Italy", 100, "xxx", 12.12345f, 12.34567f)
        val cityEntity2 = CityEntity("Madrid", "Spain", 100, "xxx", 12.12345f, 12.34567f)

        val cities = CityDataBuilder().cityDataBuilder(listOf(cityEntity, cityEntity2))
        Assert.assertNotEquals(null, cities)
        checkCityData(cities)
    }

    private fun checkCityData(worldCitiesData: List<CityData>) {
        Assert.assertNotEquals(null, worldCitiesData)
        Assert.assertThat(worldCitiesData.size, `is`(2))

        Assert.assertThat(worldCitiesData[0].name, `is`("Rome"))
        Assert.assertThat(worldCitiesData[0].country.name, `is`("Italy"))
        Assert.assertThat(worldCitiesData[0].population, `is`(100))
        Assert.assertThat(worldCitiesData[0].adminCode, `is`("xxx"))
        Assert.assertThat(worldCitiesData[0].location.latitude, `is`(12.12345f))
        Assert.assertThat(worldCitiesData[0].location.longitude, `is`(12.34567f))

        Assert.assertThat(worldCitiesData[1].name, `is`("Madrid"))
        Assert.assertThat(worldCitiesData[1].country.name, `is`("Spain"))
        Assert.assertThat(worldCitiesData[1].population, `is`(100))
        Assert.assertThat(worldCitiesData[1].adminCode, `is`("xxx"))
        Assert.assertThat(worldCitiesData[1].location.latitude, `is`(12.12345f))
        Assert.assertThat(worldCitiesData[1].location.longitude, `is`(12.34567f))
    }

}