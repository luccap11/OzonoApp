package it.luccap11.android.weatherconditions.model.data

import it.luccap11.android.weatherconditions.infrastructure.room.entities.CityEntity
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
        Assert.assertEquals(40.75123f, location.latitude)
        Assert.assertEquals(16.54321f, location.longitude)
    }

    @Test
    fun testCountry() {
        val country = Country("Italy")
        Assert.assertNotEquals(null, country)
        Assert.assertEquals("Italy", country.name)
    }

    @Test
    fun testCity() {
        val cityData = CityData("Rome", Country("Italy"), 100, "xxx", Location(12.12345f, 12.34567f))
        Assert.assertNotEquals(null, cityData)
        Assert.assertEquals("Rome", cityData.name)
        Assert.assertEquals("Italy", cityData.country.name)
        Assert.assertEquals(100, cityData.population)
        Assert.assertEquals("xxx", cityData.adminCode)
        Assert.assertEquals(12.12345f, cityData.location.latitude)
        Assert.assertEquals(12.34567f, cityData.location.longitude)
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
        Assert.assertEquals(2, worldCitiesData.size)

        Assert.assertEquals("Rome", worldCitiesData[0].name)
        Assert.assertEquals("Italy", worldCitiesData[0].country.name)
        Assert.assertEquals(100, worldCitiesData[0].population)
        Assert.assertEquals("xxx", worldCitiesData[0].adminCode)
        Assert.assertEquals(12.12345f, worldCitiesData[0].location.latitude)
        Assert.assertEquals(12.34567f, worldCitiesData[0].location.longitude)

        Assert.assertEquals("Madrid", worldCitiesData[1].name)
        Assert.assertEquals("Spain", worldCitiesData[1].country.name)
        Assert.assertEquals(100, worldCitiesData[1].population)
        Assert.assertEquals("xxx", worldCitiesData[1].adminCode)
        Assert.assertEquals(12.12345f, worldCitiesData[1].location.latitude)
        Assert.assertEquals(12.34567f, worldCitiesData[1].location.longitude)
    }

}