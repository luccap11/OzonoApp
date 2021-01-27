package it.luccap11.android.weatherconditions.model

import androidx.lifecycle.*
import it.luccap11.android.weatherconditions.infrastructure.OWeatherMapRepository
import it.luccap11.android.weatherconditions.infrastructure.Resource
import it.luccap11.android.weatherconditions.infrastructure.WorldCitiesRepository
import it.luccap11.android.weatherconditions.model.data.CityData
import it.luccap11.android.weatherconditions.model.data.WeatherData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * The ViewModel triggers the network request when the user clicks, for example, on a button
 * @author Luca Capitoli
 */
class WeatherViewModel : ViewModel() {
    val weatherLiveData = MutableLiveData<Resource<List<WeatherData>>>()
    val citiesLiveData = MutableLiveData<Resource<List<CityData>>>()
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
            cityRepository.fetchCitiesData(userQuery) { worldCities ->
                citiesLiveData.postValue(worldCities)
            }
        }
    }

}