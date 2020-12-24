package it.luccap11.android.weatherconditions.model

import androidx.lifecycle.*
import it.luccap11.android.weatherconditions.model.data.WeatherData
import it.luccap11.android.weatherconditions.service.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * The ViewModel triggers the network request when the user clicks, for example, on a button
 * @author Luca Capitoli
 */
class WeatherViewModel : ViewModel() {
    val liveData = MutableLiveData<Resource<List<WeatherData>>>()
    private val repository = Repository()

    fun updateWeatherData(selectedCity: String) {
        liveData.postValue(Resource.Loading())
        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchWeatherData(selectedCity) { weatherData ->
                liveData.postValue(weatherData)
            }
        }
    }
}