package it.luccap11.android.weatherconditions.ui.main

import android.content.Intent
import androidx.annotation.UiThread
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import it.luccap11.android.weatherconditions.R
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author Luca Capitoli
 * @since 08/mar/2021
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class SplashActivityTest {

    @Test
    @UiThread
    fun showFragment() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ActivityScenario.launch<MainActivity>(intent)
        Espresso.onView(withId(R.id.container))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(withId(R.id.container)).check { view, noViewFoundException ->
            if (noViewFoundException != null) {
                throw noViewFoundException
            }

//            val recyclerView = view as RecyclerView
//            assertThat(recyclerView.adapter).isNotNull()
//            assertThat(recyclerView.adapter!!.itemCount).isGreaterThan(0)
        }
    }
}