package com.example.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "daily_smart_tools_prefs")

class AppPreferences(private val context: Context) {

    companion object {
        val KEY_THEME_MODE = stringPreferencesKey("theme_mode") // "system", "light", "dark"
        val KEY_CURRENCY = stringPreferencesKey("currency") // default "PKR"
        val KEY_FAVORITES = stringSetPreferencesKey("favorites")
        val KEY_RECENTS = stringPreferencesKey("recents_csv") // comma-separated tool ids
    }

    val themeModeFlow: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[KEY_THEME_MODE] ?: "system"
    }

    val currencyFlow: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[KEY_CURRENCY] ?: "PKR"
    }

    val favoritesFlow: Flow<Set<String>> = context.dataStore.data.map { prefs ->
        prefs[KEY_FAVORITES] ?: setOf("calculator", "qr_scanner", "notes", "expense")
    }

    val recentsFlow: Flow<List<String>> = context.dataStore.data.map { prefs ->
        val csv = prefs[KEY_RECENTS] ?: "calculator,qr_scanner,notes"
        if (csv.isBlank()) emptyList() else csv.split(",").filter { it.isNotBlank() }
    }

    suspend fun setThemeMode(mode: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_THEME_MODE] = mode
        }
    }

    suspend fun setCurrency(currency: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_CURRENCY] = currency
        }
    }

    suspend fun toggleFavorite(toolId: String) {
        context.dataStore.edit { prefs ->
            val current = (prefs[KEY_FAVORITES] ?: setOf("calculator", "qr_scanner", "notes", "expense")).toMutableSet()
            if (current.contains(toolId)) {
                current.remove(toolId)
            } else {
                current.add(toolId)
            }
            prefs[KEY_FAVORITES] = current
        }
    }

    suspend fun recordToolUsage(toolId: String) {
        context.dataStore.edit { prefs ->
            val csv = prefs[KEY_RECENTS] ?: ""
            val list = if (csv.isBlank()) mutableListOf() else csv.split(",").filter { it.isNotBlank() }.toMutableList()
            list.remove(toolId)
            list.add(0, toolId)
            val trimmed = list.take(6)
            prefs[KEY_RECENTS] = trimmed.joinToString(",")
        }
    }
}
