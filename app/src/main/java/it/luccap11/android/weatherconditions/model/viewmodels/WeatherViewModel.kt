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
class WeatherViewModel(private val cityRepository: WorldCitiesRepository,
                       private val weatherRepository: OWeatherMapRepository) : ViewModel() {
    val weatherLiveData = MutableLiveData<Resource<List<WeatherData>>>()
    val citiesLiveData = MutableLiveData<Resource<List<CityData>>>()
    val lastCitySearched = MutableLiveData<CityData>()

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
//            cityRepository.fetchLocalCitiesData(userQuery) { worldCities ->
//                if (worldCities is Resource.Success) {
//                    citiesLiveData.postValue(worldCities)
//                } else {
                    val result = cityRepository.fetchRemoteCitiesData(userQuery)
                    citiesLiveData.postValue(Resource.Success(result))
//                }
//            }
        }
    }

    fun getLastCitySearched() {
        cityRepository.getLastCitySearched {
            lastCitySearched.postValue(it)
        }
    }

}

class WeatherViewModelFactory(private val cityRepository: WorldCitiesRepository,
                              private val weatherRepository: OWeatherMapRepository): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = WeatherViewModel(cityRepository, weatherRepository) as T
}