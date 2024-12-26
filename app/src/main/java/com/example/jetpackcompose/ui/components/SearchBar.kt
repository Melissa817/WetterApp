package com.example.jetpackcompose.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jetpackcompose.viewmodel.WeatherViewModel

/**
 * SearchBarSample is a composable function that provides a search bar with dynamic suggestions
 * and integration with a weather API. It supports recent search suggestions and different modes
 * (e.g., Home and Forecast).
 *
 * @param weatherViewModel The [WeatherViewModel] responsible for fetching weather and forecast data.
 * @param selectedMenu The currently selected menu option, used to determine the type of data to fetch.
 * @param apiKey The API key used for making requests to the weather API.
 * @param onQueryChanged Callback triggered when the search query is updated.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarSample(
    weatherViewModel: WeatherViewModel = viewModel(),
    selectedMenu: String = "",
    apiKey: String,
    onQueryChanged: (String) -> Unit
) {
    val textFieldState = rememberTextFieldState()
    var expanded by rememberSaveable { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    var cityName by rememberSaveable { mutableStateOf("") }

    var recentSearches by rememberSaveable { mutableStateOf(listOf<String>()) }
    val currentWeather by weatherViewModel.currentWeather.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 200.dp)
            .semantics { isTraversalGroup = true }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
        ) {
            // to use currentweather uncomment this part
            /*
            currentWeather?.let {
                val temperature = it.main.temp // Access the temperature
                val description = it.weather.firstOrNull()?.description ?: "No description available" // Access the description
                Text(
                    text = "Current Weather: ${temperature}°C, $description",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }
            */
            SearchBar(
                modifier = Modifier.fillMaxWidth(),
                inputField = {
                    SearchBarDefaults.InputField(
                        state = textFieldState,
                        onSearch = { inputQuery ->
                            cityName = inputQuery
                            onQueryChanged(cityName)

                            if (inputQuery.isNotEmpty()) {
                                when (selectedMenu) {
                                    "Home" -> weatherViewModel.fetchWeatherData(inputQuery, apiKey)
                                    "Forecast" -> weatherViewModel.fetchForecastData(inputQuery, apiKey)
                                }
                                recentSearches = (listOf(inputQuery) + recentSearches).distinct().take(5)
                            }

                            focusManager.clearFocus()
                            textFieldState.clearText()
                            expanded = false
                        },
                        expanded = expanded,
                        onExpandedChange = {
                            expanded = it && recentSearches.isNotEmpty()
                        },
                        placeholder = { Text("Search any city") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        trailingIcon = { Icon(Icons.Default.MoreVert, contentDescription = null) }
                    )
                },
                expanded = expanded,
                onExpandedChange = {
                    expanded = it && recentSearches.isNotEmpty()
                },
            ) {
                if (recentSearches.isNotEmpty() && expanded) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 300.dp)
                            .padding(8.dp)
                    ) {
                        items(recentSearches.size) { idx ->
                            val resultText = recentSearches[idx]
                            ListItem(
                                headlineContent = { Text(resultText) },
                                leadingContent = { Icon(Icons.Filled.Star , contentDescription = null) },
                                colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                                modifier = Modifier
                                    .clickable {
                                        textFieldState.setTextAndPlaceCursorAtEnd(resultText)
                                        expanded = false
                                        weatherViewModel.fetchWeatherData(resultText, apiKey)
                                        recentSearches = (listOf(resultText) + recentSearches).distinct().take(5)
                                        focusManager.clearFocus()
                                    }
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}