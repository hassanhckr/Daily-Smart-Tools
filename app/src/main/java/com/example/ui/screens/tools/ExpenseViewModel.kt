package com.example.ui.screens.tools

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.ExpenseEntity
import com.example.data.repository.ExpensesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId

data class CategorySummary(
    val category: String,
    val total: Double,
    val percentage: Float
)

class ExpenseViewModel(private val repository: ExpensesRepository) : ViewModel() {

    // Single source of truth for expenses from database
    val allExpenses: StateFlow<List<ExpenseEntity>> = repository.allExpenses.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Derived directly from in-memory allExpenses Flow (no extra DB queries)
    val totalExpenseAmount: StateFlow<Double> = allExpenses.map { list ->
        list.sumOf { it.amount }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    val currentMonthTotal: StateFlow<Double> = allExpenses.map { list ->
        val now = LocalDate.now()
        val firstDayOfMonth = now.withDayOfMonth(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        list.filter { it.date >= firstDayOfMonth }.sumOf { it.amount }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    val todayTotal: StateFlow<Double> = allExpenses.map { list ->
        val todayStart = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        list.filter { it.date >= todayStart }.sumOf { it.amount }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    val categorySummaries: StateFlow<List<CategorySummary>> = allExpenses.map { list ->
        val total = list.sumOf { it.amount }
        if (total == 0.0) {
            emptyList()
        } else {
            list.groupBy { it.category }.map { (cat, items) ->
                val catSum = items.sumOf { it.amount }
                CategorySummary(
                    category = cat,
                    total = catSum,
                    percentage = (catSum / total).toFloat()
                )
            }.sortedByDescending { it.total }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun addExpense(title: String, amount: Double, category: String, date: Long, notes: String = "") {
        viewModelScope.launch {
            repository.insert(
                ExpenseEntity(
                    title = title.trim(),
                    amount = amount,
                    category = category,
                    date = date,
                    notes = notes.trim()
                )
            )
        }
    }

    fun updateExpense(expense: ExpenseEntity) {
        viewModelScope.launch {
            repository.update(expense)
        }
    }

    fun deleteExpense(expense: ExpenseEntity) {
        viewModelScope.launch {
            repository.delete(expense)
        }
    }
}
