package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val amount: Double,
    val category: String, // "Food", "Travel", "Shopping", "Bills", "Entertainment", "Health", "Other"
    val date: Long = System.currentTimeMillis(),
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
