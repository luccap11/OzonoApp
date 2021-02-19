package it.luccap11.android.weatherconditions.model.data

import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.Mockito.lenient
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
        assertEquals("List should be empty", emptyList<CityData>(), cache.getCachedCitiesData("xxx"))
    }

    @Test
    fun getCachedCitiesData_FilledCache() {
        val city = "Umea"
        cache.addCachedCityData(city, listOf(cityData, cityData2))
        assertEquals("List should contain 2 cities", listOf(cityData, cityData2), cache.getCachedCitiesData(city))
        assertEquals("List should be empty", emptyList<CityData>(), cache.getCachedCitiesData("xxx"))
    }

    @Test
    fun addCachedCityDataTest() {
        val city = "Madrid"
        cache.addCachedCityData(city, listOf(cityData, cityData2))
        assertEquals("List should contain 2 cities", listOf(cityData, cityData2), cache.getCachedCitiesData(city))

        cache.addCachedCityData(city, listOf(cityData, cityData2))
        assertEquals("List should contain 4 cities", listOf(cityData, cityData2, cityData, cityData2), cache.getCachedCitiesData(city))
    }

    @Test
    fun deleteMultipleCachedCityDataTest_SingleEntry() {
        val city = "Umea"
        cache.addCachedCityData(city, listOf(cityData, cityData2))
        cache.deleteMultipleCachedCityData(city)

        assertEquals("List should be empty", emptyList<CityData>(), cache.getCachedCitiesData(city))
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

        assertEquals("List should be empty", emptyList<CityData>(), cache.getCachedCitiesData("U"))
        assertEquals("List should be empty", emptyList<CityData>(), cache.getCachedCitiesData("Um"))
        assertEquals("List should be empty", emptyList<CityData>(), cache.getCachedCitiesData("Ume"))
        assertEquals("List should be empty", emptyList<CityData>(), cache.getCachedCitiesData("Umea"))

        assertEquals("List should be empty", emptyList<CityData>(), cache.getCachedCitiesData("R"))
        assertEquals("List should be empty", emptyList<CityData>(), cache.getCachedCitiesData("Ro"))
        assertEquals("List should be empty", emptyList<CityData>(), cache.getCachedCitiesData("Rom"))
        assertEquals("List should contain 2 cities", listOf(cityData, cityData2), cache.getCachedCitiesData("Rome"))
    }

    @Test
    fun isDataInCacheTest() {
        val city = "Edimburgh"
        cache.addCachedCityData(city, listOf(cityData, cityData2))
        assertEquals(true, cache.isDataInCache(city))
        assertEquals(false, cache.isDataInCache("xxx"))
    }

}