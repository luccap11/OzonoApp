package it.luccap11.android.ozono.repository

import it.luccap11.android.ozono.CoroutinesTestRule
import androidx.test.core.app.ApplicationProvider
import it.luccap11.android.ozono.OzonoAppl
import it.luccap11.android.ozono.TestUtil
import it.luccap11.android.ozono.model.data.CitiesDataCache
import it.luccap11.android.ozono.model.data.source.FakeCitiesDao
import it.luccap11.android.ozono.network.AlgoliaCitiesRemoteDataSource
import it.luccap11.android.ozono.util.PreferencesManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.core.Is.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.robolectric.RobolectricTestRunner

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class WorldCitiesRepositoryRobolectricTest {
    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()
    @Mock
    private val cache = mock(CitiesDataCache::class.java)
    @Mock
    private val remoteRepository = mock(AlgoliaCitiesRemoteDataSource::class.java)
    @Mock
    private val preferenceManager = mock(PreferencesManager::class.java)
    private var fakeCitiesDao = FakeCitiesDao()

    @Before
    fun beforeTests() {
        ApplicationProvider.getApplicationContext<OzonoAppl>()
    }

    @Test
    fun getLastCitySearchedTest_PrefSavedButNoResultOnDb() = coroutinesTestRule.testDispatcher.runBlockingTest {
        `when`(preferenceManager.getLastSearchedCityLatit()).thenReturn(12.3456f)
        `when`(preferenceManager.getLastSearchedCityLongit()).thenReturn(12.3456f)

        val  methodUnderTest = WorldCitiesRepository(cache, fakeCitiesDao, remoteRepository, preferenceManager)

        val results = methodUnderTest.getLastCitySearched()

        assertEquals(results, null)
    }

    @Test
    fun getLastCitySearchedTest_ok() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val cityEntity = TestUtil().mockCityEntity(System.currentTimeMillis())
        fakeCitiesDao = FakeCitiesDao(mutableListOf(cityEntity, cityEntity, cityEntity, cityEntity))

        `when`(preferenceManager.getLastSearchedCityLatit()).thenReturn(12.3456f)
        `when`(preferenceManager.getLastSearchedCityLongit()).thenReturn(12.3456f)

        val  methodUnderTest = WorldCitiesRepository(cache, fakeCitiesDao, remoteRepository, preferenceManager)

        val results = methodUnderTest.getLastCitySearched()

        assertNotEquals(results, null)
        assertThat(results!!.country.name, `is`("Portogallo"))
        assertThat(results.localeNames.cityNames[0], `is`("Lisbona"))
        assertThat(results.region[0], `is`("admin code"))
        assertThat(results.geoloc.lat, `is`(12.3456f))
        assertThat(results.geoloc.lng, `is`(12.3456f))
    }

    @Test
    fun getLastCitySearchedTest_noPrefSaved() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val  methodUnderTest = WorldCitiesRepository(cache, fakeCitiesDao, remoteRepository, preferenceManager)

        val results = methodUnderTest.getLastCitySearched()

        assertEquals(results, null)
    }
}