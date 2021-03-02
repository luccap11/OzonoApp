package it.luccap11.android.weatherconditions.model.viewmodels

import androidx.lifecycle.*
import it.luccap11.android.weatherconditions.infrastructure.OWeatherMapRepository
import it.luccap11.android.weatherconditions.infrastructure.Resource
import it.luccap11.android.weatherconditions.infrastructure.WorldCitiesRepository
import it.luccap11.android.weatherconditions.model.data.CityData
import it.luccap11.android.weatherconditions.model.data.WeatherData
import kotlinx.coroutines.*

/**
 * The ViewModel triggers the network request when the user clicks, for example, on a button
 * @author Luca Capitoli
 */
class WeatherViewModel : ViewModel() {
    val weatherLiveData = MutableLiveData<Resource<List<WeatherData>>>()
    val citiesLiveData = MutableLiveData<Resource<List<CityData>>>()
    val lastCitySearched = MutableLiveData<CityData>()
    private val weatherRepository = OWeatherMapRepository()
    private val cityRepository = WorldCitiesRepository()

    fun updateWeatherData(selectedCity: String) {
        weatherLiveData.postValue(Resource.Loading())
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.fetchWeatherData(selectedCity) { weatherData ->
                weatherLiveData.postValue(weatherData)
            }
        }
    }

    fun updateCityData(userQuery: String) {
        citiesLiveData.postValue(Resource.Loading())
        viewModelScope.launch(Dispatchers.IO) {
            cityRepository.fetchLocalCitiesData(userQuery) { worldCities ->
                if (worldCities is Resource.Success) {
                    citiesLiveData.postValue(worldCities)
                } else {
                    cityRepository.fetchRemoteCitiesData(userQuery) {
                        citiesLiveData.postValue(it)
                    }
                }
            }
        }
    }

    fun getLastCitySearched() {
        cityRepository.getLastCitySearched {
            lastCitySearched.postValue(it)
        }
    }

}