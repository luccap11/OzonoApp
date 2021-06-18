package it.luccap11.android.ozono.infrastructure.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import it.luccap11.android.ozono.infrastructure.room.daos.CitiesDao
import it.luccap11.android.ozono.util.TestUtil
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * @author Luca Capitoli
 * @since 04/mar/2021
 */
@RunWith(AndroidJUnit4::class)
class CityEntityTest {
    private lateinit var cityDao: CitiesDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java).build()
        cityDao = db.citiesDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertOneCityTest() = runBlocking {
        val city = TestUtil.createCities(1).apply {
            get(0).name = "Tokio"
            get(0).country = "Japan"
        }
        cityDao.insertCities(*city)
        val byCoords = cityDao.findCityByCoords(0f, 0f)
        val byNotValidCoords = cityDao.findCityByCoords(23f, 42f)
        assertThat(byCoords, equalTo(city[0]))
        assertThat(byNotValidCoords, equalTo(null))
    }

    @Test
    @Throws(Exception::class)
    fun insertMultipleCityTest() = runBlocking {
        val city = TestUtil.createCities(2).apply {
            get(0).name = "Tokio"
            get(0).country = "Japan"
            get(1).name = "Udine"
            get(1).country = "Italy"
        }
        cityDao.insertCities(*city)
        val byCoords = cityDao.findCityByCoords(0f, 0f)
        val byCoords2 = cityDao.findCityByCoords(1f, 1f)
        assertThat(byCoords, equalTo(city[0]))
        assertThat(byCoords2, equalTo(city[1]))
    }

    @Test
    @Throws(Exception::class)
    fun findCitiesStartWithTest() = runBlocking {
        val city = TestUtil.createCities(5).apply {
            get(0).name = "Tokio"
            get(1).name = "Toronto"
            get(2).name = "Turin"
            get(3).name = "Tricesimo"
            get(4).name = "Ankara"
        }
        cityDao.insertCities(*city)
        val startWith = cityDao.findCitiesStartWith("T%", 3)
        val startWith2 = cityDao.findCitiesStartWith("To%", 0)
        val startWith3 = cityDao.findCitiesStartWith("To%", 42)
        val startWith4 = cityDao.findCitiesStartWith("xxxxx%", 42)
        assertThat(startWith, equalTo(arrayListOf(city[3], city[2], city[1])))
        assertThat(startWith2, equalTo(arrayListOf()))
        assertThat(startWith3, equalTo(arrayListOf(city[1], city[0])))
        assertThat(startWith4, equalTo(arrayListOf()))
    }
}