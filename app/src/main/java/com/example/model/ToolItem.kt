package com.example.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class ToolItem(
    val id: String,
    val title: String,
    val category: String,
    val description: String,
    val route: String,
    val icon: ImageVector,
    val pastelColor: Color
)

object ToolsData {
    val allTools = listOf(
        ToolItem(
            id = "calculator",
            title = "Calculator",
            category = "Math & Finance",
            description = "Standard math with decimals, percent & history",
            route = "tool_calculator",
            icon = Icons.Default.Calculate,
            pastelColor = Color(0xFFC7A1EE)
        ),
        ToolItem(
            id = "age_calculator",
            title = "Age Calculator",
            category = "Utility",
            description = "Exact age in years, months, days & countdown",
            route = "tool_age_calculator",
            icon = Icons.Default.CalendarToday,
            pastelColor = Color(0xFFE9B8D5)
        ),
        ToolItem(
            id = "qr_scanner",
            title = "QR Scanner",
            category = "Utility",
            description = "Instant camera QR code detection & actions",
            route = "tool_qr_scanner",
            icon = Icons.Default.QrCodeScanner,
            pastelColor = Color(0xFF8BB7F0)
        ),
        ToolItem(
            id = "qr_generator",
            title = "QR Generator",
            category = "Utility",
            description = "Generate, save & share custom QR codes",
            route = "tool_qr_generator",
            icon = Icons.Default.QrCode,
            pastelColor = Color(0xFF88C999)
        ),
        ToolItem(
            id = "discount_calc",
            title = "Discount Calculator",
            category = "Math & Finance",
            description = "Compute sale price and total savings",
            route = "tool_discount_calculator",
            icon = Icons.Default.LocalOffer,
            pastelColor = Color(0xFFF4988F)
        ),
        ToolItem(
            id = "percent_calc",
            title = "Percentage Calculator",
            category = "Math & Finance",
            description = "Multi-mode percentage, changes & ratios",
            route = "tool_percent_calculator",
            icon = Icons.Default.Percent,
            pastelColor = Color(0xFFF7C268)
        ),
        ToolItem(
            id = "unit_converter",
            title = "Unit Converter",
            category = "Utility",
            description = "Weight, length, temp & volume conversions",
            route = "tool_unit_converter",
            icon = Icons.Default.SwapHoriz,
            pastelColor = Color(0xFFBCA6EA)
        ),
        ToolItem(
            id = "bmi_calc",
            title = "BMI Calculator",
            category = "Health",
            description = "Body mass index, health ranges & gauge",
            route = "tool_bmi_calculator",
            icon = Icons.Default.FitnessCenter,
            pastelColor = Color(0xFF88C999)
        ),
        ToolItem(
            id = "notes",
            title = "Notes",
            category = "Productivity",
            description = "Fast local scratchpad with search & timestamps",
            route = "tool_notes",
            icon = Icons.Default.Description,
            pastelColor = Color(0xFFE9B8D5)
        ),
        ToolItem(
            id = "expense",
            title = "Expense Tracker",
            category = "Math & Finance",
            description = "Track expenses by category with monthly totals",
            route = "tool_expense",
            icon = Icons.Default.Paid,
            pastelColor = Color(0xFFA77BD1)
        ),
        ToolItem(
            id = "password_gen",
            title = "Password Generator",
            category = "Utility",
            description = "SecureRandom passwords with customizable rules",
            route = "tool_password_generator",
            icon = Icons.Default.Key,
            pastelColor = Color(0xFF8BB7F0)
        ),
        ToolItem(
            id = "date_calc",
            title = "Date Calculator",
            category = "Utility",
            description = "Days between dates, add/subtract time",
            route = "tool_date_calculator",
            icon = Icons.Default.CalendarMonth,
            pastelColor = Color(0xFFF7C268)
        )
    )

    fun getToolById(id: String): ToolItem? = allTools.firstOrNull { it.id == id }
}
