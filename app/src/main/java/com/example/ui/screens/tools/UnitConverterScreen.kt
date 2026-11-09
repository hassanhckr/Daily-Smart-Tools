package com.example.ui.screens.tools

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CommonTopBar
import com.example.ui.components.CustomCard
import com.example.ui.theme.BrandPink
import com.example.ui.theme.BrandPurple
import java.text.DecimalFormat

enum class UnitCategory(val label: String, val units: List<String>) {
    Length(
        "Length",
        listOf("Meters", "Kilometers", "Centimeters", "Millimeters", "Miles", "Feet", "Inches", "Yards")
    ),
    Weight(
        "Weight",
        listOf("Kilograms", "Grams", "Milligrams", "Pounds", "Ounces")
    ),
    Temperature(
        "Temperature",
        listOf("Celsius", "Fahrenheit", "Kelvin")
    ),
    Volume(
        "Volume",
        listOf("Liters", "Milliliters", "Gallons (US)", "Cups (US)", "Fluid Ounces (US)")
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnitConverterScreen(
    onBackClick: () -> Unit
) {
    BackHandler(onBack = onBackClick)

    var selectedCategoryIndex by remember { mutableIntStateOf(0) }
    val currentCategory = UnitCategory.values()[selectedCategoryIndex]

    var fromUnit by remember(currentCategory) { mutableStateOf(currentCategory.units[0]) }
    var toUnit by remember(currentCategory) { mutableStateOf(currentCategory.units.getOrElse(1) { currentCategory.units[0] }) }
    var fromValueStr by remember { mutableStateOf("1") }

    val fromValue = fromValueStr.toDoubleOrNull() ?: 0.0
    val convertedValue = convertUnits(fromValue, fromUnit, toUnit, currentCategory)

    val df = DecimalFormat("#,##0.######")

    Scaffold(
        topBar = {
            CommonTopBar(
                title = "Unit Converter",
                onBackClick = onBackClick
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // Category Tabs
            PrimaryScrollableTabRow(
                selectedTabIndex = selectedCategoryIndex,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary,
                edgePadding = 16.dp
            ) {
                UnitCategory.values().forEachIndexed { index, cat ->
                    Tab(
                        selected = selectedCategoryIndex == index,
                        onClick = { selectedCategoryIndex = index },
                        text = {
                            Text(
                                text = cat.label,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (selectedCategoryIndex == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // From Unit Card
                CustomCard(modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "From",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        UnitDropdown(
                            label = "Select Unit",
                            units = currentCategory.units,
                            selectedUnit = fromUnit,
                            onUnitSelected = { fromUnit = it }
                        )

                        OutlinedTextField(
                            value = fromValueStr,
                            onValueChange = { fromValueStr = it },
                            label = { Text("Enter Value") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("unit_from_input"),
                            shape = RoundedCornerShape(14.dp)
                        )
                    }
                }

                // Swap Units Button
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = {
                            val temp = fromUnit
                            fromUnit = toUnit
                            toUnit = temp
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(BrandPurple)
                            .testTag("unit_swap_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.SwapVert,
                            contentDescription = "Swap Units",
                            tint = Color.White
                        )
                    }
                }

                // To Unit Card
                CustomCard(modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "To",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        UnitDropdown(
                            label = "Select Unit",
                            units = currentCategory.units,
                            selectedUnit = toUnit,
                            onUnitSelected = { toUnit = it }
                        )
                    }
                }

                // Result Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(22.dp))
                        .background(
                            Brush.linearGradient(listOf(BrandPurple, BrandPink))
                        )
                        .padding(22.dp)
                ) {
                    Column {
                        Text(
                            text = "$fromValueStr $fromUnit =",
                            color = Color.White.copy(alpha = 0.85f),
                            style = MaterialTheme.typography.labelMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "${df.format(convertedValue)} $toUnit",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 28.sp
                            ),
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnitDropdown(
    label: String,
    units: List<String>,
    selectedUnit: String,
    onUnitSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedUnit,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            shape = RoundedCornerShape(14.dp)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            units.forEach { unit ->
                DropdownMenuItem(
                    text = { Text(unit) },
                    onClick = {
                        onUnitSelected(unit)
                        expanded = false
                    }
                )
            }
        }
    }
}

fun convertUnits(value: Double, from: String, to: String, category: UnitCategory): Double {
    if (from == to) return value

    return when (category) {
        UnitCategory.Length -> {
            // Convert to meters
            val meters = when (from) {
                "Meters" -> value
                "Kilometers" -> value * 1000.0
                "Centimeters" -> value / 100.0
                "Millimeters" -> value / 1000.0
                "Miles" -> value * 1609.344
                "Feet" -> value * 0.3048
                "Inches" -> value * 0.0254
                "Yards" -> value * 0.9144
                else -> value
            }
            // Convert from meters
            when (to) {
                "Meters" -> meters
                "Kilometers" -> meters / 1000.0
                "Centimeters" -> meters * 100.0
                "Millimeters" -> meters * 1000.0
                "Miles" -> meters / 1609.344
                "Feet" -> meters / 0.3048
                "Inches" -> meters / 0.0254
                "Yards" -> meters / 0.9144
                else -> meters
            }
        }

        UnitCategory.Weight -> {
            // Convert to grams
            val grams = when (from) {
                "Kilograms" -> value * 1000.0
                "Grams" -> value
                "Milligrams" -> value / 1000.0
                "Pounds" -> value * 453.59237
                "Ounces" -> value * 28.3495231
                else -> value
            }
            // Convert from grams
            when (to) {
                "Kilograms" -> grams / 1000.0
                "Grams" -> grams
                "Milligrams" -> grams * 1000.0
                "Pounds" -> grams / 453.59237
                "Ounces" -> grams / 28.3495231
                else -> grams
            }
        }

        UnitCategory.Temperature -> {
            // Convert to Celsius
            val celsius = when (from) {
                "Celsius" -> value
                "Fahrenheit" -> (value - 32.0) * 5.0 / 9.0
                "Kelvin" -> value - 273.15
                else -> value
            }
            // Convert from Celsius
            when (to) {
                "Celsius" -> celsius
                "Fahrenheit" -> (celsius * 9.0 / 5.0) + 32.0
                "Kelvin" -> celsius + 273.15
                else -> celsius
            }
        }

        UnitCategory.Volume -> {
            // Convert to Milliliters
            val ml = when (from) {
                "Liters" -> value * 1000.0
                "Milliliters" -> value
                "Gallons (US)" -> value * 3785.41178
                "Cups (US)" -> value * 236.588236
                "Fluid Ounces (US)" -> value * 29.5735296
                else -> value
            }
            // Convert from Milliliters
            when (to) {
                "Liters" -> ml / 1000.0
                "Milliliters" -> ml
                "Gallons (US)" -> ml / 3785.41178
                "Cups (US)" -> ml / 236.588236
                "Fluid Ounces (US)" -> ml / 29.5735296
                else -> ml
            }
        }
    }
}
