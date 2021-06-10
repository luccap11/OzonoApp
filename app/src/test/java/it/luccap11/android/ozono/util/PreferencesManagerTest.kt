package it.luccap11.android.ozono.util

import android.content.Context
import android.content.SharedPreferences
import androidx.test.core.app.ApplicationProvider
import it.luccap11.android.ozono.OzonoAppl
import it.luccap11.android.ozono.R
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PreferencesManagerTest {
    private lateinit var context: Context
    private lateinit var sharedPrefs: SharedPreferences

    @Before
    fun beforeTests() {
        context = ApplicationProvider.getApplicationContext<OzonoAppl>()
        sharedPrefs = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE)
    }

    @Test
    fun getLastSearchedCityLatit() {
        val pref = PreferencesManager().getLastSearchedCityLatit()
        val sharedPref = sharedPrefs.getFloat(context.getString(R.string.preference_last_searched_city_latit),
            AppUtils.NOT_SET.toFloat()
        )
        assertEquals(pref, AppUtils.NOT_SET.toFloat())
        assertEquals(pref, sharedPref)
    }

    @Test
    fun saveLastSearchedCityLatit() {
        val manager = PreferencesManager()
        manager.saveLastSearchedCityLatit(12.3456f)

        val pref = manager.getLastSearchedCityLatit()
        assertEquals(pref, 12.3456f)
    }

    @Test
    fun saveLastSearchedCityLongit_noSave() {
        val pref = PreferencesManager().getLastSearchedCityLongit()
        val sharedPref = sharedPrefs.getFloat(context.getString(R.string.preference_last_searched_city_longit),
            AppUtils.NOT_SET.toFloat()
        )

        assertEquals(pref, AppUtils.NOT_SET.toFloat())
        assertEquals(pref, sharedPref)
    }

    @Test
    fun saveLastSearchedCityLongit() {
        val manager = PreferencesManager()
        manager.saveLastSearchedCityLongit(12.3456f)

        val pref = manager.getLastSearchedCityLongit()
        assertEquals(pref, 12.3456f)
    }
}