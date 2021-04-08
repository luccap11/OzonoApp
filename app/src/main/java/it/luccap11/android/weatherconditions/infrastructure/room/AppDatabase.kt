package it.luccap11.android.weatherconditions.infrastructure.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import it.luccap11.android.weatherconditions.OzonoAppl
import it.luccap11.android.weatherconditions.infrastructure.room.daos.CitiesDao
import it.luccap11.android.weatherconditions.infrastructure.room.entities.CityEntity


/**
 * @author Luca Capitoli
 * @since 29/jan/2021
 */
@Database(entities = [CityEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        private const val DB_NAME = "ozono.db"
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase().also { instance = it }
            }
        }

        // Create and pre-populate the database. See this article for more details:
        // https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1#4785
        private fun buildDatabase(): AppDatabase {
            return Room.databaseBuilder(OzonoAppl.instance, AppDatabase::class.java, DB_NAME).build()
        }
    }

    abstract fun citiesDao(): CitiesDao
}