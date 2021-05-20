package it.luccap11.android.ozono.model.data

import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner


/**
 * @author Luca Capitoli
 * @since 02/jan/2021
 */
@RunWith(MockitoJUnitRunner::class)
class CitiesDataCacheTest {

    private val cityData = Mockito.mock(CityData::class.java)
    private val cityData2 = Mockito.mock(CityData::class.java)
    private var cache = CitiesDataCache

    @Test
    fun getCachedCitiesData_EmptyCache() {
        assertThat("List should be empty", cache.getCachedCitiesData("xxx"), `is`(emptyList()))
    }

    @Test
    fun getCachedCitiesData_FilledCache() {
        val city = "Umea"
        cache.addCachedCityData(city, listOf(cityData, cityData2))
        assertThat("List should contain 2 cities", cache.getCachedCitiesData(city), `is`(listOf(cityData, cityData2)))
        assertThat("List should be empty", cache.getCachedCitiesData("xxx"), `is`(emptyList()))
    }

    @Test
    fun addCachedCityDataTest() {
        val city = "Madrid"
        cache.addCachedCityData(city, listOf(cityData, cityData2))
        assertThat("List should contain 2 cities", cache.getCachedCitiesData(city), `is`(listOf(cityData, cityData2)))

        cache.addCachedCityData(city, listOf(cityData, cityData2))
        assertThat("List should contain 4 cities", cache.getCachedCitiesData(city), `is`(listOf(cityData, cityData2, cityData, cityData2)))
    }

    @Test
    fun deleteMultipleCachedCityDataTest_SingleEntry() {
        val city = "Umea"
        cache.addCachedCityData(city, listOf(cityData, cityData2))
        cache.deleteMultipleCachedCityData(city)

        assertThat("List should be empty", cache.getCachedCitiesData(city), `is`(emptyList()))
    }

    @Test
    fun deleteMultipleCachedCityDataTest_MultipleEntry() {
        cache.addCachedCityData("U", listOf(cityData, cityData2))
        cache.addCachedCityData("Um", listOf(cityData, cityData2))
        cache.addCachedCityData("Ume", listOf(cityData, cityData2))
        cache.addCachedCityData("Umea", listOf(cityData, cityData2))

        cache.addCachedCityData("R", listOf(cityData, cityData2))
        cache.addCachedCityData("Ro", listOf(cityData, cityData2))
        cache.addCachedCityData("Rom", listOf(cityData, cityData2))
        cache.addCachedCityData("Rome", listOf(cityData, cityData2))
        cache.deleteMultipleCachedCityData("Umea")
        cache.deleteMultipleCachedCityData("Rom")


        //non mi convincono i test. Se cambio il metodo testato, loro passano comunque...

        assertThat("List should be empty", cache.getCachedCitiesData("U"), `is`(emptyList()))
        assertThat("List should be empty", cache.getCachedCitiesData("Um"), `is`(emptyList()))
        assertThat("List should be empty", cache.getCachedCitiesData("Ume"), `is`(emptyList()))
        assertThat("List should be empty", cache.getCachedCitiesData("Umea"), `is`(emptyList()))

        assertThat("List should be empty", cache.getCachedCitiesData("R"), `is`(emptyList()))
        assertThat("List should be empty", cache.getCachedCitiesData("Ro"), `is`(emptyList()))
        assertThat("List should be empty", cache.getCachedCitiesData("Rom"), `is`(emptyList()))
        assertThat("List should contain 2 cities", cache.getCachedCitiesData("Rome"), `is`(listOf(cityData, cityData2)))
    }

    @Test
    fun isDataInCacheTest() {
        val city = "Edimburgh"
        cache.addCachedCityData(city, listOf(cityData, cityData2))
        assertTrue(cache.isDataInCache(city))
        assertFalse(cache.isDataInCache("xxx"))
    }

}