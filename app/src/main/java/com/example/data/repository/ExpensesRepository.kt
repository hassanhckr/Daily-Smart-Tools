package com.example.data.repository

import com.example.data.local.ExpenseDao
import com.example.data.local.ExpenseEntity
import kotlinx.coroutines.flow.Flow

class ExpensesRepository(private val expenseDao: ExpenseDao) {
    val allExpenses: Flow<List<ExpenseEntity>> = expenseDao.getAllExpenses()

    fun getExpensesBetweenDates(startDate: Long, endDate: Long): Flow<List<ExpenseEntity>> =
        expenseDao.getExpensesBetweenDates(startDate, endDate)

    val totalAmount: Flow<Double?> = expenseDao.getTotalAmount()

    suspend fun insert(expense: ExpenseEntity): Long = expenseDao.insertExpense(expense)

    suspend fun update(expense: ExpenseEntity) = expenseDao.updateExpense(expense)

    suspend fun delete(expense: ExpenseEntity) = expenseDao.deleteExpense(expense)

    suspend fun deleteById(id: Long) = expenseDao.deleteExpenseById(id)
}
