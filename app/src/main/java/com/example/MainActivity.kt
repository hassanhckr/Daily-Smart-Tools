package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.AppPreferences
import com.example.navigation.AppNavigation
import com.example.ui.theme.DailySmartToolsTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val appPreferences = remember { AppPreferences(applicationContext) }
            val themeMode by appPreferences.themeModeFlow.collectAsStateWithLifecycle(initialValue = "system")
            val currency by appPreferences.currencyFlow.collectAsStateWithLifecycle(initialValue = "PKR")
            val scope = rememberCoroutineScope()

            val systemInDark = isSystemInDarkTheme()
            val isDarkTheme = when (themeMode) {
                "dark" -> true
                "light" -> false
                else -> systemInDark
            }

            DailySmartToolsTheme(darkTheme = isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(
                        appPreferences = appPreferences,
                        currentThemeMode = themeMode,
                        currentCurrency = currency,
                        onThemeModeChanged = { newMode ->
                            scope.launch { appPreferences.setThemeMode(newMode) }
                        },
                        onCurrencyChanged = { newCurrency ->
                            scope.launch { appPreferences.setCurrency(newCurrency) }
                        },
                        isDarkTheme = isDarkTheme
                    )
                }
            }
        }
    }
}
