package it.luccap11.android.ozono.repository

import it.luccap11.android.ozono.CoroutinesTestRule
import it.luccap11.android.ozono.TestUtil
import it.luccap11.android.ozono.infrastructure.room.daos.CitiesDao
import it.luccap11.android.ozono.model.data.CitiesDataCache
import it.luccap11.android.ozono.model.data.source.FakeCitiesDao
import it.luccap11.android.ozono.network.AlgoliaCitiesRemoteDataSource
import it.luccap11.android.ozono.utils.PreferencesManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsNot.not
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.*

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class WorldCitiesRepositoryTest {
    private val numOfCitiesResults = 3
    private lateinit var classUnderTest : WorldCitiesRepository
    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()
    @Mock
    private val cache = mock(CitiesDataCache::class.java)
    @Mock
    private val dao = mock(CitiesDao::class.java)
    @Mock
    private val remoteRepository = mock(AlgoliaCitiesRemoteDataSource::class.java)
    @Mock
    private val preferenceManager = mock(PreferencesManager::class.java)

    @Before
    fun beforeTest() {
        classUnderTest = WorldCitiesRepository(cache, dao, remoteRepository, preferenceManager)
    }

    @Test
    fun fetchLocalCitiesData_cache() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val city = TestUtil().mockCityData()
        `when`(cache.getCachedCitiesData(anyString())).thenReturn(listOf(city))

        val results = classUnderTest.fetchLocalCitiesData("Lisbona", numOfCitiesResults)

        assertNotEquals(results, null)
        assertThat(results.size, `is`(1))
    }

    @Test
    fun fetchLocalCitiesData_empty_db() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val fakeCitiesDao = FakeCitiesDao()
        classUnderTest = WorldCitiesRepository(cache, fakeCitiesDao, remoteRepository, preferenceManager)
        `when`(cache.getCachedCitiesData(anyString())).thenReturn(emptyList())

        val results = classUnderTest.fetchLocalCitiesData("Lisbona", numOfCitiesResults)

        assertNotEquals(results, null)
        assertThat(results, `is`(emptyList()))
    }

    @Test
    fun fetchLocalCitiesData_2orLess_db() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val city = TestUtil().mockCityEntity(System.currentTimeMillis())
        val fakeCitiesDao = FakeCitiesDao(mutableListOf(city, city))
        classUnderTest = WorldCitiesRepository(cache, fakeCitiesDao, remoteRepository, preferenceManager)
        `when`(cache.getCachedCitiesData(anyString())).thenReturn(emptyList())

        val results = classUnderTest.fetchLocalCitiesData("Lisbona", numOfCitiesResults)

        assertNotEquals(results, null)
        assertThat(results, `is`(emptyList()))
    }

    @Test
    fun fetchLocalCitiesData_3orMore_db() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val city = TestUtil().mockCityEntity(System.currentTimeMillis())
        val fakeCitiesDao = FakeCitiesDao(mutableListOf(city, city, city, city))
        classUnderTest = WorldCitiesRepository(cache, fakeCitiesDao, remoteRepository, preferenceManager)
        `when`(cache.getCachedCitiesData(anyString())).thenReturn(emptyList())

        val results = classUnderTest.fetchLocalCitiesData("Lisbona", numOfCitiesResults)

        assertNotEquals(results, null)
        assertThat(results, not(emptyList()))
        assertThat(results[0].country.name, `is`("Portogallo"))
        assertThat(results[0].localeNames.cityNames[0], `is`("Lisbona"))
        assertThat(results[0].region[0], `is`("admin code"))
        assertThat(results[0].geoloc.lat, `is`(12.3456f))
        assertThat(results[0].geoloc.lng, `is`(12.3456f))
    }

    @Test
    fun fetchRemoteCitiesData_null() = coroutinesTestRule.testDispatcher.runBlockingTest {
        `when`(remoteRepository.fetchAlgoliaData(anyString())).thenReturn(null)

        val results = classUnderTest.fetchRemoteCitiesData("Lisbona", numOfCitiesResults)

        assertEquals(results, null)
    }

    @Test
    fun fetchRemoteCitiesData_empty() = coroutinesTestRule.testDispatcher.runBlockingTest {
        `when`(remoteRepository.fetchAlgoliaData(anyString())).thenReturn(emptyList())

        val results = classUnderTest.fetchRemoteCitiesData("Lisbona", numOfCitiesResults)

        assertNotEquals(results, null)
        assertThat(results, `is`(emptyList()))
    }

    @Test
    fun fetchRemoteCitiesData() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val cityData = TestUtil().mockCityData()
        val cityEntity = TestUtil().mockCityEntity(System.currentTimeMillis())
        val fakeCitiesDao = FakeCitiesDao(mutableListOf(cityEntity, cityEntity, cityEntity, cityEntity))
        classUnderTest = WorldCitiesRepository(cache, fakeCitiesDao, remoteRepository, preferenceManager)
        `when`(remoteRepository.fetchAlgoliaData(anyString())).thenReturn(listOf(cityData))

        val results = classUnderTest.fetchRemoteCitiesData("Lisbona", numOfCitiesResults)

        assertNotEquals(results, null)
        assertThat(results!!.size, `is`(3))
    }

    @Test
    fun fetchRemoteCitiesData_emptyDao() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val cityData = TestUtil().mockCityData()
        val fakeCitiesDao = FakeCitiesDao()
        classUnderTest = WorldCitiesRepository(cache, fakeCitiesDao, remoteRepository, preferenceManager)
        `when`(remoteRepository.fetchAlgoliaData(anyString())).thenReturn(listOf(cityData))

        val results = classUnderTest.fetchRemoteCitiesData("Lisbona", numOfCitiesResults)

        assertNotEquals(results, null)
        assertThat(results, `is`(emptyList()))
    }
}