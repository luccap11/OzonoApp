package it.luccap11.android.weatherconditions.infrastructure.room

import android.content.Context
import androidx.annotation.NonNull
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import it.luccap11.android.weatherconditions.infrastructure.room.daos.CitiesDao
import it.luccap11.android.weatherconditions.infrastructure.room.entities.CityEntity


/**
 * @author Luca Capitoli
 * @since 29/jan/2021
 */
@Database(entities = [CityEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        const val DB_NAME = "app name"
        private var INSTANCE: AppDatabase? = null

        @NonNull
        fun getDatabase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class.java) {
                    if (INSTANCE == null) {
                        // Create database here
                        INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, DB_NAME).build()
                    }
                }
            }
            return INSTANCE
        }
    }

    abstract fun citiesDao(): CitiesDao
}