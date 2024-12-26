package com.example.jetpackcompose.api

import android.util.Log
import com.example.jetpackcompose.data.ForecastData
import com.example.jetpackcompose.data.WeatherData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * WeatherApiService is a singleton object that facilitates communication with the OpenWeatherMap API.
 * It provides functions to fetch current weather data and weather forecasts.
 */
object WeatherApiService {
    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    // OkHttpClient for network operations
    private val client = OkHttpClient.Builder().build()

    // Retrofit instance for API communication
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // API interface for defining endpoints
    private val api = retrofit.create(WeatherApi::class.java)

    /**
     * WeatherApi interface defines the API endpoints for fetching weather data.
     */
    interface WeatherApi {
        /**
         * Fetches current weather data for a specified city.
         *
         * @param city The name of the city to fetch weather data for.
         * @param apiKey The API key for authenticating the request.
         * @param units The unit system to use (default is "metric").
         * @return A Response object containing WeatherData if successful, null otherwise.
         */
        @GET("weather")
        suspend fun fetchWeather(
            @Query("q") city: String,
            @Query("appid") apiKey: String,
            @Query("units") units: String = "metric"
        ): retrofit2.Response<WeatherData>

        /**
         * Fetches weather forecast data for a specified city.
         *
         * @param city The name of the city to fetch forecast data for.
         * @param apiKey The API key for authenticating the request.
         * @param units The unit system to use (default is "metric").
         * @return A Response object containing ForecastData if successful, null otherwise.
         */
        @GET("forecast")
        suspend fun fetchForecast(
            @Query("q") city: String,
            @Query("appid") apiKey: String,
            @Query("units") units: String = "metric"
        ): retrofit2.Response<ForecastData>
    }

    /**
     * Fetches current weather data for a specified city.
     *
     * @param city The name of the city to fetch weather data for.
     * @param apiKey The API key for authenticating the request.
     * @return WeatherData object if successful, null otherwise.
     */
    suspend fun fetchWeather(city: String, apiKey: String): WeatherData? {
        return try {
            withContext(Dispatchers.Default) {
                val response = api.fetchWeather(city, apiKey)
                if (response.isSuccessful) {
                    response.body()
                } else {
                    Log.e("WeatherApiService", "Failed to fetch data: ${response.code()}")
                    null
                }
            }
        } catch (e: Exception) {
            Log.e("WeatherApiService", "Error fetching data: ${e.message}")
            null
        }
    }

    /**
     * Fetches weather forecast data for a specified city.
     *
     * @param city The name of the city to fetch forecast data for.
     * @param apiKey The API key for authenticating the request.
     * @return ForecastData object if successful, null otherwise.
     */
    suspend fun fetchForecast(city: String, apiKey: String): ForecastData? {
        return try {
            withContext(Dispatchers.IO) {
                val response = api.fetchForecast(city, apiKey)
                if (response.isSuccessful) {
                    response.body()
                } else {
                    Log.e("WeatherApiService", "Failed to fetch forecast: ${response.code()}")
                    null
                }
            }
        } catch (e: Exception) {
            Log.e("WeatherApiService", "Error fetching forecast: ${e.message}")
            null
        }
    }
}