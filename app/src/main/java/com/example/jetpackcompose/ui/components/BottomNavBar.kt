package com.example.jetpackcompose.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * BottomNavBar
 * A customizable bottom navigation bar for a Jetpack Compose application. It allows for easy navigation
 * between different sections of the app, such as Home, Forecast, and Settings.
 * @param selectedItem The index of the currently selected item.
 * @param onItemSelected Callback invoked when a navigation item is selected, with the index of the selected item.
 * @param modifier [Modifier] to be applied to the BottomNavigation component.
 * @param backgroundColor The background color of the navigation bar.
 * @param selectedTintColor The tint color for selected navigation items.
 * @param unselectedTintColor The tint color for unselected navigation items.
 */
@Composable
fun BottomNavBar(
    selectedItem: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    selectedTintColor: Color = Color.White,
    unselectedTintColor: Color = Color.Black
) {
    BottomNavigation(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = backgroundColor
    ) {
        BottomNavigationItem(
            selected = selectedItem == 0,
            onClick = { onItemSelected(0) },
            label = { Text("Home") },
            icon = { NavigationIcon(Icons.Filled.Home, selectedItem == 0, selectedTintColor, unselectedTintColor) }
        )
        BottomNavigationItem(
            selected = selectedItem == 1,
            onClick = { onItemSelected(1) },
            label = { Text("Forecast") },
            icon = { NavigationIcon(Icons.Filled.Schedule, selectedItem == 1, selectedTintColor, unselectedTintColor) }
        )
        BottomNavigationItem(
            selected = selectedItem == 2,
            onClick = { onItemSelected(2) },
            label = { Text("Settings") },
            icon = { NavigationIcon(Icons.Filled.Settings, selectedItem == 2, selectedTintColor, unselectedTintColor) }
        )
    }
}
/**
 * NavigationIcon
 * A composable function that displays an icon with a tint color based on its selection state.
 * @param icon The [ImageVector] representing the icon to display.
 * @param isSelected Whether the icon is currently selected.
 * @param selectedTintColor The tint color for the icon when it is selected.
 * @param unselectedTintColor The tint color for the icon when it is not selected.
 */
@Composable
fun NavigationIcon(
    icon: ImageVector,
    isSelected: Boolean,
    selectedTintColor: Color,
    unselectedTintColor: Color
) {
    Icon(
        imageVector = icon,
        contentDescription = null,
        tint = if (isSelected) selectedTintColor else unselectedTintColor
    )
}
