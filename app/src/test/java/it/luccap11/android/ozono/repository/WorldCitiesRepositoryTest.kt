package it.luccap11.android.ozono.repository

import CoroutinesTestRule
import it.luccap11.android.ozono.TestUtil
import it.luccap11.android.ozono.infrastructure.room.daos.CitiesDao
import it.luccap11.android.ozono.model.data.CitiesDataCache
import it.luccap11.android.ozono.network.AlgoliaCitiesRemoteDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.core.Is.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertNotEquals
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
    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()
    @Mock
    private val cache = mock(CitiesDataCache::class.java)
    @Mock
    private val dao = mock(CitiesDao::class.java)
    @Mock
    private val remoteRepository = mock(AlgoliaCitiesRemoteDataSource::class.java)

    @Test
    fun fetchLocalCitiesData_cache() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val city = TestUtil().mockCityData()
        val  methodUnderTest = WorldCitiesRepository(cache, dao, remoteRepository)
        `when`(cache.getCachedCitiesData(anyString())).thenReturn(listOf(city))

        val results = methodUnderTest.fetchLocalCitiesData("Lisbona", numOfCitiesResults)

        assertNotEquals(results, null)
        assertThat(results.size, `is`(1))
    }
}