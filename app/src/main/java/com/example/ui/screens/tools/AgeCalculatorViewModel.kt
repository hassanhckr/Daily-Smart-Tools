package com.example.ui.screens.tools

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

data class AgeResult(
    val years: Int,
    val months: Int,
    val days: Int,
    val totalDays: Long,
    val totalMonths: Long,
    val totalWeeks: Long,
    val totalHours: Long,
    val nextBirthdayMonths: Int,
    val nextBirthdayDays: Int,
    val nextBirthdayDayOfWeek: String,
    val formattedDob: String
)

data class AgeCalculatorUiState(
    val selectedDate: LocalDate? = null,
    val ageResult: AgeResult? = null,
    val errorMessage: String? = null
)

class AgeCalculatorViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AgeCalculatorUiState())
    val uiState: StateFlow<AgeCalculatorUiState> = _uiState.asStateFlow()

    init {
        // Default example birth date (e.g., 25 years ago)
        val defaultDob = LocalDate.now().minusYears(25).minusMonths(3).minusDays(14)
        setDateOfBirth(defaultDob)
    }

    fun setDateOfBirth(dob: LocalDate) {
        val today = LocalDate.now()
        if (dob.isAfter(today)) {
            _uiState.value = AgeCalculatorUiState(
                selectedDate = dob,
                errorMessage = "Birth date cannot be in the future!"
            )
            return
        }

        val period = Period.between(dob, today)
        val totalDays = ChronoUnit.DAYS.between(dob, today)
        val totalMonths = ChronoUnit.MONTHS.between(dob, today)
        val totalWeeks = totalDays / 7
        val totalHours = totalDays * 24

        // Next Birthday calculation
        var nextBday = dob.withYear(today.year)
        if (nextBday.isBefore(today) || nextBday.isEqual(today)) {
            nextBday = nextBday.plusYears(1)
        }
        val nextBdayPeriod = Period.between(today, nextBday)
        val dayOfWeek = nextBday.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }

        val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")

        val result = AgeResult(
            years = period.years,
            months = period.months,
            days = period.days,
            totalDays = totalDays,
            totalMonths = totalMonths,
            totalWeeks = totalWeeks,
            totalHours = totalHours,
            nextBirthdayMonths = nextBdayPeriod.months,
            nextBirthdayDays = nextBdayPeriod.days,
            nextBirthdayDayOfWeek = dayOfWeek,
            formattedDob = dob.format(formatter)
        )

        _uiState.value = AgeCalculatorUiState(
            selectedDate = dob,
            ageResult = result,
            errorMessage = null
        )
    }
}
