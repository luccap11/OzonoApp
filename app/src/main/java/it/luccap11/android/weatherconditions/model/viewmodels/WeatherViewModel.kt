package it.luccap11.android.weatherconditions.model.viewmodels

import androidx.lifecycle.*
import it.luccap11.android.weatherconditions.OzonoAppl
import it.luccap11.android.weatherconditions.R
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
class WeatherViewModel(
    private val cityRepository: WorldCitiesRepository,
    private val weatherRepository: OWeatherMapRepository
) : ViewModel() {
    private val resources = OzonoAppl.instance.resources
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
            val localCities = cityRepository.fetchLocalCitiesData(userQuery)
            if (localCities.isNotEmpty()) {
                citiesLiveData.postValue(Resource.Success(localCities))
            } else {
                val remoteCities = cityRepository.fetchRemoteCitiesData(userQuery)
                if (remoteCities == null) {
                    citiesLiveData.postValue(Resource.Error(resources.getString(R.string.error_label)))
                } else {
                    citiesLiveData.postValue(Resource.Success(remoteCities))
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
    private val weatherRepository: OWeatherMapRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        WeatherViewModel(cityRepository, weatherRepository) as T
}