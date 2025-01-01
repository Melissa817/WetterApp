package com.example.jetpackcompose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcompose.api.WeatherApiService
import com.example.jetpackcompose.data.ForecastItem
import com.example.jetpackcompose.data.WeatherData
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * WeatherViewModel is a ViewModel that manages weather-related data for the application.
 * It fetches current weather and forecast data from the WeatherApiService and exposes
 * this data as state flows for the UI to observe.
 */
class WeatherViewModel : ViewModel() {

    // Mutable state flow for current weather data
    private val _currentWeather = MutableStateFlow<WeatherData?>(null)
    val currentWeather: StateFlow<WeatherData?> = _currentWeather

    // Mutable state flow for forecast data
    private val _forecast = MutableStateFlow<List<ForecastItem>>(emptyList())
    val forecast: StateFlow<List<ForecastItem>> = _forecast

    // Mutable state flow for the weather icon URL
    private val _iconUrl = MutableStateFlow<String?>(null)
    val iconUrl: StateFlow<String?> get() = _iconUrl

    // Mutable state flow for error messages
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    /**
     * Fetches current weather data for the specified city using the provided API key.
     * @param city The name of the city for which to fetch weather data.
     * @param apiKey The API key for authenticating the request.
     */
    fun fetchWeatherData(city: String, apiKey: String) {
        viewModelScope.launch {
            try {
                val weatherResponse = WeatherApiService.fetchWeather(city, apiKey)
                if (weatherResponse != null) {
                    _currentWeather.value = weatherResponse
                    fetchWeatherIcon(weatherResponse.weather.firstOrNull()?.icon.orEmpty())
                    _errorMessage.value = null
                } else {
                    _errorMessage.value = "Failed to fetch weather. Please check your API key or city name."
                }
            } catch (e: Exception) {
                _errorMessage.value = "An error occurred: ${e.localizedMessage}"
            }
        }
    }

    /**
     * Fetches forecast data for the specified city using the provided API key.
     * @param city The name of the city for which to fetch forecast data.
     * @param apiKey The API key for authenticating the request.
     */
    fun fetchForecastData(city: String, apiKey: String) {
        viewModelScope.launch {
            try {
                val forecastResponse = WeatherApiService.fetchForecast(city, apiKey)
                if (forecastResponse != null) {
                    _forecast.value = forecastResponse.list
                    _errorMessage.value = null
                } else {
                    _errorMessage.value = "Failed to fetch forecast. Please check your API key or city name."
                }
            } catch (e: Exception) {
                _errorMessage.value = "An error occurred: ${e.localizedMessage}"
            }
        }
    }

    /**
     * Fetches the URL for the weather icon based on the provided icon ID.
     * @param iconId The ID of the weather icon.
     */
    private fun fetchWeatherIcon(iconId: String) {
        if (iconId.isNotEmpty()) {
            _iconUrl.value = "https://openweathermap.org/img/wn/$iconId@2x.png"
        }
    }
}