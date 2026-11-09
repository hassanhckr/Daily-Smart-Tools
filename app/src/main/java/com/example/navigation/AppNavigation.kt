package com.example.navigation

import android.app.Activity
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ads.AdManager
import com.example.data.local.AppDatabase
import com.example.data.local.AppPreferences
import com.example.data.repository.ExpensesRepository
import com.example.data.repository.NotesRepository
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.home.HomeViewModel
import com.example.ui.screens.settings.AboutScreen
import com.example.ui.screens.settings.PrivacyPolicyScreen
import com.example.ui.screens.settings.SettingsScreen
import com.example.ui.screens.splash.SplashScreen
import com.example.ui.screens.tools.AgeCalculatorScreen
import com.example.ui.screens.tools.AgeCalculatorViewModel
import com.example.ui.screens.tools.BmiCalculatorScreen
import com.example.ui.screens.tools.CalculatorScreen
import com.example.ui.screens.tools.CalculatorViewModel
import com.example.ui.screens.tools.DateCalculatorScreen
import com.example.ui.screens.tools.DiscountCalculatorScreen
import com.example.ui.screens.tools.ExpenseScreen
import com.example.ui.screens.tools.ExpenseViewModel
import com.example.ui.screens.tools.NotesScreen
import com.example.ui.screens.tools.NotesViewModel
import com.example.ui.screens.tools.PasswordGeneratorScreen
import com.example.ui.screens.tools.PercentageCalculatorScreen
import com.example.ui.screens.tools.QrGeneratorScreen
import com.example.ui.screens.tools.QrScannerScreen
import com.example.ui.screens.tools.UnitConverterScreen

object Routes {
    const val SPLASH = "splash"
    const val HOME = "home"
    const val TOOL_CALCULATOR = "tool_calculator"
    const val TOOL_AGE_CALCULATOR = "tool_age_calculator"
    const val TOOL_QR_SCANNER = "tool_qr_scanner"
    const val TOOL_QR_GENERATOR = "tool_qr_generator"
    const val TOOL_DISCOUNT_CALCULATOR = "tool_discount_calculator"
    const val TOOL_PERCENT_CALCULATOR = "tool_percent_calculator"
    const val TOOL_UNIT_CONVERTER = "tool_unit_converter"
    const val TOOL_BMI_CALCULATOR = "tool_bmi_calculator"
    const val TOOL_NOTES = "tool_notes"
    const val TOOL_EXPENSE = "tool_expense"
    const val TOOL_PASSWORD_GENERATOR = "tool_password_generator"
    const val TOOL_DATE_CALCULATOR = "tool_date_calculator"
    const val SETTINGS = "settings"
    const val ABOUT = "about"
    const val PRIVACY_POLICY = "privacy_policy"
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    appPreferences: AppPreferences,
    currentThemeMode: String,
    currentCurrency: String,
    onThemeModeChanged: (String) -> Unit,
    onCurrencyChanged: (String) -> Unit,
    isDarkTheme: Boolean
) {
    val context = LocalContext.current
    val activity = context as? Activity

    val database = remember { AppDatabase.getDatabase(context) }
    val notesRepo = remember { NotesRepository(database.noteDao()) }
    val expensesRepo = remember { ExpensesRepository(database.expenseDao()) }

    val adManager = remember { AdManager(context) }

    fun popWithAdCheck() {
        navController.popBackStack()
        adManager.notifyActionCompleted(activity)
    }

    // Soft, premium 400ms animations with 0.96 scale and 8dp gentle vertical motion
    val animationDuration = 400
    val exitDuration = 320
    val softEasing = CubicBezierEasing(0.16f, 1f, 0.3f, 1f)
    val exitEasing = CubicBezierEasing(0.4f, 0f, 1f, 1f)

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH,
        enterTransition = {
            fadeIn(
                animationSpec = tween(durationMillis = animationDuration, easing = softEasing)
            ) + scaleIn(
                initialScale = 0.96f,
                animationSpec = tween(durationMillis = animationDuration, easing = softEasing)
            ) + slideInVertically(
                initialOffsetY = { 24 }, // gentle ~8dp vertical motion
                animationSpec = tween(durationMillis = animationDuration, easing = softEasing)
            )
        },
        exitTransition = {
            fadeOut(
                animationSpec = tween(durationMillis = exitDuration, easing = exitEasing)
            ) + scaleOut(
                targetScale = 0.98f,
                animationSpec = tween(durationMillis = exitDuration, easing = exitEasing)
            )
        },
        popEnterTransition = {
            fadeIn(
                animationSpec = tween(durationMillis = animationDuration, easing = softEasing)
            ) + scaleIn(
                initialScale = 0.98f,
                animationSpec = tween(durationMillis = animationDuration, easing = softEasing)
            )
        },
        popExitTransition = {
            fadeOut(
                animationSpec = tween(durationMillis = exitDuration, easing = exitEasing)
            ) + scaleOut(
                targetScale = 0.96f,
                animationSpec = tween(durationMillis = exitDuration, easing = exitEasing)
            ) + slideOutVertically(
                targetOffsetY = { 24 },
                animationSpec = tween(durationMillis = exitDuration, easing = exitEasing)
            )
        }
    ) {
        composable(Routes.SPLASH) {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.HOME) {
            val homeViewModel = remember { HomeViewModel(appPreferences) }
            HomeScreen(
                viewModel = homeViewModel,
                onNavigateToTool = { route, _ ->
                    navController.navigate(route)
                },
                onNavigateToSettings = {
                    navController.navigate(Routes.SETTINGS)
                },
                onToggleTheme = {
                    val nextTheme = if (isDarkTheme) "light" else "dark"
                    onThemeModeChanged(nextTheme)
                },
                isDarkTheme = isDarkTheme
            )
        }

        composable(Routes.TOOL_CALCULATOR) {
            val calculatorViewModel: CalculatorViewModel = viewModel()
            CalculatorScreen(
                viewModel = calculatorViewModel,
                onBackClick = { popWithAdCheck() }
            )
        }

        composable(Routes.TOOL_AGE_CALCULATOR) {
            val ageCalculatorViewModel: AgeCalculatorViewModel = viewModel()
            AgeCalculatorScreen(
                viewModel = ageCalculatorViewModel,
                onBackClick = { popWithAdCheck() }
            )
        }

        composable(Routes.TOOL_QR_SCANNER) {
            QrScannerScreen(
                onBackClick = { popWithAdCheck() }
            )
        }

        composable(Routes.TOOL_QR_GENERATOR) {
            QrGeneratorScreen(
                onBackClick = { popWithAdCheck() }
            )
        }

        composable(Routes.TOOL_DISCOUNT_CALCULATOR) {
            DiscountCalculatorScreen(
                currencyCode = currentCurrency,
                onBackClick = { popWithAdCheck() }
            )
        }

        composable(Routes.TOOL_PERCENT_CALCULATOR) {
            PercentageCalculatorScreen(
                onBackClick = { popWithAdCheck() }
            )
        }

        composable(Routes.TOOL_UNIT_CONVERTER) {
            UnitConverterScreen(
                onBackClick = { popWithAdCheck() }
            )
        }

        composable(Routes.TOOL_BMI_CALCULATOR) {
            BmiCalculatorScreen(
                onBackClick = { popWithAdCheck() }
            )
        }

        composable(Routes.TOOL_NOTES) {
            val notesViewModel = remember { NotesViewModel(notesRepo) }
            NotesScreen(
                viewModel = notesViewModel,
                onBackClick = { popWithAdCheck() }
            )
        }

        composable(Routes.TOOL_EXPENSE) {
            val expenseViewModel = remember { ExpenseViewModel(expensesRepo) }
            ExpenseScreen(
                viewModel = expenseViewModel,
                currencyCode = currentCurrency,
                onBackClick = { popWithAdCheck() }
            )
        }

        composable(Routes.TOOL_PASSWORD_GENERATOR) {
            PasswordGeneratorScreen(
                onBackClick = { popWithAdCheck() }
            )
        }

        composable(Routes.TOOL_DATE_CALCULATOR) {
            DateCalculatorScreen(
                onBackClick = { popWithAdCheck() }
            )
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(
                currentThemeMode = currentThemeMode,
                currentCurrency = currentCurrency,
                onThemeModeChanged = onThemeModeChanged,
                onCurrencyChanged = onCurrencyChanged,
                onNavigateToAbout = { navController.navigate(Routes.ABOUT) },
                onNavigateToPrivacy = { navController.navigate(Routes.PRIVACY_POLICY) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Routes.ABOUT) {
            AboutScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Routes.PRIVACY_POLICY) {
            PrivacyPolicyScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
