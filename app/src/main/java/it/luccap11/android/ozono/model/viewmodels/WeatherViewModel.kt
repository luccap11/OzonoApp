package it.luccap11.android.ozono.model.viewmodels

import androidx.lifecycle.*
import it.luccap11.android.ozono.model.ApiStatus
import it.luccap11.android.ozono.repository.WeatherDataRepository
import it.luccap11.android.ozono.repository.WorldCitiesRepository
import it.luccap11.android.ozono.model.data.CityData
import it.luccap11.android.ozono.model.data.ListData
import kotlinx.coroutines.*

/**
 * The ViewModel triggers the network request when the user clicks, for example, on a button
 * @author Luca Capitoli
 */
class WeatherViewModel(
    private val cityRepository: WorldCitiesRepository,
    private val weatherRepository: WeatherDataRepository
) : ViewModel() {
    private val numOfCitiesResults = 3
    val lastCitySearched = MutableLiveData<CityData>()

    val citiesStatus = MutableLiveData<ApiStatus>()
    private val _citiesData = MutableLiveData<List<CityData>>()
    val citiesData: LiveData<List<CityData>> = _citiesData

    val weatherStatus = MutableLiveData<ApiStatus>()
    private val _weatherData = MutableLiveData<List<ListData>>()
    val weatherData: LiveData<List<ListData>> = _weatherData

    fun updateWeatherData(selectedCity: String) {
        weatherStatus.postValue(ApiStatus.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            val fiveDaysWeather = weatherRepository.fetchWeatherDataByCityName(selectedCity)
            if (fiveDaysWeather == null) {
                return@launch weatherStatus.postValue(ApiStatus.ERROR)
            } else {
                _weatherData.postValue(temp_filterData(fiveDaysWeather))
                return@launch weatherStatus.postValue(ApiStatus.SUCCESS)
            }
        }
    }

    private fun temp_filterData(original: List<ListData>): List<ListData> {
        val result = mutableListOf<ListData>()
        for (index in original.indices step 8) {
            result.add(original[index])
        }
        return result
    }

    fun updateCityData(userQuery: String) {
        citiesStatus.postValue(ApiStatus.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            val localCities = cityRepository.fetchLocalCitiesData(userQuery, numOfCitiesResults)
            if (localCities.isNotEmpty()) {
                _citiesData.postValue(localCities)
                citiesStatus.postValue(ApiStatus.SUCCESS)
            } else {
                val remoteCities = cityRepository.fetchRemoteCitiesData(userQuery, numOfCitiesResults)
                if (remoteCities == null) {
                    citiesStatus.postValue(ApiStatus.ERROR)
                } else {
                    _citiesData.postValue(remoteCities.take(numOfCitiesResults))
                    citiesStatus.postValue(ApiStatus.SUCCESS)
                }
            }
        }
    }

    fun getLastCitySearched() {
        viewModelScope.launch(Dispatchers.IO) {
            lastCitySearched.postValue(cityRepository.getLastCitySearched())
        }
    }

}

class WeatherViewModelFactory(
    private val cityRepository: WorldCitiesRepository,
    private val weatherRepository: WeatherDataRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = WeatherViewModel(cityRepository, weatherRepository) as T
}