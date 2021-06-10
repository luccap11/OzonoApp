package it.luccap11.android.ozono.utils

import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import it.luccap11.android.ozono.infrastructure.room.entities.CityEntity
import org.hamcrest.Matcher
import org.hamcrest.core.AllOf

/**
 * @author Luca Capitoli
 * @since 04/mar/2021
 */
object TestUtil {

    fun createCities(number: Int, vararg cityName: String): Array<CityEntity> {
        val result = mutableListOf<CityEntity>()
        for (i in 0 until number) {
            result.add(CityEntity(cityName[i], "", i, "b", i.toFloat(), i.toFloat(), System.currentTimeMillis()))
        }
        return result.toTypedArray()
    }

    fun typeSearchViewText(text: String): ViewAction {
        return object : ViewAction {
            override fun getDescription(): String {
                return "Change view text"
            }

            override fun getConstraints(): Matcher<View> {
                return AllOf.allOf(
                    ViewMatchers.isDisplayed(),
                    ViewMatchers.isAssignableFrom(SearchView::class.java)
                )
            }

            override fun perform(uiController: UiController?, view: View?) {
                (view as SearchView).setQuery(text, false)
            }
        }
    }
}