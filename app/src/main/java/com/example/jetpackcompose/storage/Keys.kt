package com.example.jetpackcompose.storage

import androidx.datastore.preferences.core.stringPreferencesKey
/**
 * Keys
 * An object that centralizes the definition of keys used in the application's DataStore preferences.
 * This ensures consistency and avoids hardcoding string keys throughout the codebase.
 */
object Keys {
    /** Key to store and retrieve the user's hometown. */
    val HOMETOWN_KEY = stringPreferencesKey("hometown_key")
    /** Key to store and retrieve the API token for network requests. */
    val API_TOKEN_KEY = stringPreferencesKey("api_token_key")
    /** Key to store and retrieve the timer option selected by the user. */
    val TIMER_OPTION_KEY = stringPreferencesKey("timer_option_key")
}
