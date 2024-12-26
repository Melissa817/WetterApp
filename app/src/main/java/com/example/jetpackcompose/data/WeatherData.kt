package com.example.jetpackcompose.data

/**
 * Represents the current weather data returned from the weather API.
 *
 * @property coord An instance of [Coord] representing the geographical coordinates (longitude and latitude).
 * @property weather A list of [Weather] objects containing weather conditions.
 * @property base A string representing the internal parameter for the API.
 * @property main An instance of [Main] containing main weather parameters.
 * @property visibility An integer representing the visibility in meters.
 * @property wind An instance of [Wind] containing wind information.
 * @property clouds An instance of [Clouds] containing cloudiness information.
 * @property dt A long representing the date and time of the weather data (in Unix timestamp format).
 * @property sys An instance of [Sys] containing additional system information.
 * @property timezone An integer representing the timezone offset from UTC.
 * @property id An integer representing the city ID.
 * @property name A string representing the name of the city.
 * @property cod An integer representing the response code from the API.
 */
data class WeatherData(
    val coord: Coord,
    val weather: List<Weather>,
    val base: String,
    val main: Main,
    val visibility: Int,
    val wind: Wind,
    val clouds: Clouds,
    val dt: Long,
    val sys: Sys,
    val timezone: Int,
    val id: Int,
    val name: String,
    val cod: Int
)

/**
 * Represents geographical coordinates.
 *
 * @property lon A double representing the longitude.
 * @property lat A double representing the latitude.
 */
data class Coord(val lon: Double, val lat: Double)

/**
 * Represents weather conditions.
 *
 * @property id An integer representing the weather condition ID.
 * @property main A string representing the main weather condition (e.g., "Clear", "Rain").
 * @property description A string providing a more detailed description of the weather condition.
 * @property icon A string representing the icon code for the weather condition.
 */
data class Weather(val id: Int, val main: String, val description: String, val icon: String)

/**
 * Represents main weather parameters.
 *
 * @property temp A double representing the current temperature.
 * @property feels_like A double representing the perceived temperature.
 * @property temp_min A double representing the minimum temperature at the moment.
 * @property temp_max A double representing the maximum temperature at the moment.
 * @property pressure An integer representing the atmospheric pressure in hPa.
 * @property humidity An integer representing the humidity percentage.
 */
data class Main(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int
)

/**
 * Represents wind information.
 *
 * @property speed A double representing the wind speed.
 * @property deg An integer representing the wind direction in degrees.
 * @property gust A double representing the wind gust speed.
 */
data class Wind(val speed: Double, val deg: Int, val gust: Double)

/**
 * Represents cloudiness information.
 *
 * @property all An integer representing the percentage of cloud cover.
 */
data class Clouds(val all: Int)

/**
 * Represents additional system information for the weather data.
 *
 * @property type An integer representing the type of the system.
 * @property id An integer representing the system ID.
 * @property country A string representing the country code.
 * @property sunrise A long representing the sunrise time (in Unix timestamp format).
 * @property sunset A long representing the sunset time (in Unix timestamp format).
 */
data class Sys(
    val type: Int,
    val id: Int,
    val country: String,
    val sunrise: Long,
    val sunset: Long
)