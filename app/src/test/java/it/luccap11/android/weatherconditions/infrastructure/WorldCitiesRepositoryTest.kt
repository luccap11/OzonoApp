package it.luccap11.android.weatherconditions.infrastructure

import it.luccap11.android.weatherconditions.infrastructure.room.AppDatabase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito

/**
 * @author Luca Capitoli
 * @since 22/feb/2021
 */
@RunWith(JUnit4::class)
class WorldCitiesRepositoryTest {
    private lateinit var db : AppDatabase

    @Before
    fun before() {
        db = Mockito.mock(AppDatabase::class.java)
    }

    @Test
    fun fetchLocalCitiesDataTest() {

    }
}