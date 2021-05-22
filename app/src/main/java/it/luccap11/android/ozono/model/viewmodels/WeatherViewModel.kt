package it.luccap11.android.ozono.model.viewmodels

import androidx.lifecycle.*
import it.luccap11.android.ozono.OzonoAppl
import it.luccap11.android.ozono.R
import it.luccap11.android.ozono.infrastructure.ApiStatus
import it.luccap11.android.ozono.repository.WeatherDataRepository
import it.luccap11.android.ozono.infrastructure.Resource
import it.luccap11.android.ozono.repository.WorldCitiesRepository
import it.luccap11.android.ozono.model.data.CityData
import it.luccap11.android.ozono.model.data.WeatherData
import kotlinx.coroutines.*

/**
 * The ViewModel triggers the network request when the user clicks, for example, on a button
 * @author Luca Capitoli
 */
class WeatherViewModel(
    private val cityRepository: WorldCitiesRepository,
    private val weatherRepository: WeatherDataRepository
) : ViewModel() {
    private val resources = OzonoAppl.instance.resources
//    val weatherLiveData = MutableLiveData<Resource<List<WeatherData>>>()
    val citiesLiveData = MutableLiveData<Resource<List<CityData>>>()
    val lastCitySearched = MutableLiveData<CityData>()

    val weatherStatus = MutableLiveData<ApiStatus>()
    private val _weatherData = MutableLiveData<List<WeatherData>>()
    val weatherData: LiveData<List<WeatherData>> = _weatherData

    fun updateWeatherData(selectedCity: String) {
        weatherStatus.postValue(ApiStatus.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            val fiveDaysWeather = weatherRepository.fetchWeatherDataByCityName(selectedCity)
            if (fiveDaysWeather == null) {
                return@launch weatherStatus.postValue(ApiStatus.ERROR)
            } else {
                _weatherData.postValue(fiveDaysWeather!!)
                return@launch weatherStatus.postValue(ApiStatus.SUCCESS)
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
    private val weatherRepository: WeatherDataRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = WeatherViewModel(cityRepository, weatherRepository) as T
}