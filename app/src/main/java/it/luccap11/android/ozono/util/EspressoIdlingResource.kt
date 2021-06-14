package it.luccap11.android.ozono.util

import androidx.test.espresso.idling.CountingIdlingResource

/**
 * Note: It is unusual to have testing code in your application. To understand more about why, and
 * methods of removing idling resource code from your application production code, check out Android
 * testing with Espresso's Idling Resources and testing fidelity.
 */
object EspressoIdlingResource {

    private const val RESOURCE = "GLOBAL"

    @JvmField
    val countingIdlingResource = CountingIdlingResource(RESOURCE)

    fun increment() {
        countingIdlingResource.increment()
    }

    fun decrement() {
        if (!countingIdlingResource.isIdleNow) {
            countingIdlingResource.decrement()
        }
    }
}

inline fun <T> wrapEspressoIdlingResource(function: () -> T): T {
    // Espresso does not work well with coroutines yet. See
    // https://github.com/Kotlin/kotlinx.coroutines/issues/982
    EspressoIdlingResource.increment() // Set app as busy.
    return try {
        function()
    } finally {
        EspressoIdlingResource.decrement() // Set app as idle.
    }
}