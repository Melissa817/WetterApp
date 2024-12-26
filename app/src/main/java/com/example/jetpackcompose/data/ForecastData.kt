package com.example.jetpackcompose.data

/**
 * Represents the forecast data returned from the weather API.
 *
 * @property cod A string representing the response code from the API.
 * @property message An integer representing the message associated with the response.
 * @property cnt An integer representing the number of forecast items returned.
 * @property list A list of [ForecastItem] representing the individual forecast entries.
 */
data class ForecastData(
    val cod: String,
    val message: Int,
    val cnt: Int,
    val list: List<ForecastItem>
)

/**
 * Represents an individual forecast item.
 *
 * @property dt A long representing the date and time of the forecast (in Unix timestamp format).
 * @property main An instance of [Main] containing main weather parameters.
 * @property weather A list of [Weather] objects containing weather conditions.
 * @property clouds An instance of [Clouds] containing cloudiness information.
 * @property wind An instance of [Wind] containing wind information.
 * @property visibility An integer representing the visibility in meters.
 * @property pop A double representing the probability of precipitation.
 * @property sys An instance of [ForecastSys] containing additional system information.
 * @property dt_txt A string representing the date and time of the forecast in a human-readable format.
 * @property rain An optional instance of [Rain] containing rain volume information for the last 3 hours.
 */
data class ForecastItem(
    val dt: Long,
    val main: Main,
    val weather: List<Weather>,
    val clouds: Clouds,
    val wind: Wind,
    val visibility: Int,
    val pop: Double,
    val sys: ForecastSys,
    val dt_txt: String,
    val rain: Rain? = null
)

/**
 * Represents additional system information for the forecast.
 *
 * @property pod A string representing the part of the day (e.g., "d" for day, "n" for night).
 */
data class ForecastSys(val pod: String)

/**
 * Represents rain volume information for the forecast.
 *
 * @property `3h` A double representing the volume of rain in the last 3 hours (optional).
 */
data class Rain(val `3h`: Double? = null)