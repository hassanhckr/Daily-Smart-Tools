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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.ui.theme.PastelAmber
import com.example.ui.theme.PastelCoral
import com.example.ui.theme.PastelGreen
import java.text.DecimalFormat

@Composable
fun BmiCalculatorScreen(
    onBackClick: () -> Unit
) {
    BackHandler(onBack = onBackClick)

    var isMetric by remember { mutableStateOf(true) }

    // Metric inputs
    var heightCmStr by remember { mutableStateOf("175") }
    var weightKgStr by remember { mutableStateOf("70") }

    // Imperial inputs
    var heightFtStr by remember { mutableStateOf("5") }
    var heightInStr by remember { mutableStateOf("9") }
    var weightLbsStr by remember { mutableStateOf("154") }

    val heightMeters: Double = if (isMetric) {
        (heightCmStr.toDoubleOrNull() ?: 0.0) / 100.0
    } else {
        val ft = heightFtStr.toDoubleOrNull() ?: 0.0
        val inches = heightInStr.toDoubleOrNull() ?: 0.0
        ((ft * 12.0) + inches) * 0.0254
    }

    val weightKg: Double = if (isMetric) {
        weightKgStr.toDoubleOrNull() ?: 0.0
    } else {
        (weightLbsStr.toDoubleOrNull() ?: 0.0) * 0.45359237
    }

    val bmi: Double = if (heightMeters > 0) weightKg / (heightMeters * heightMeters) else 0.0

    val (category, categoryColor) = when {
        bmi <= 0.0 -> "Enter valid details" to MaterialTheme.colorScheme.onSurfaceVariant
        bmi < 18.5 -> "Underweight" to PastelAmber
        bmi < 25.0 -> "Normal Weight" to PastelGreen
        bmi < 30.0 -> "Overweight" to PastelAmber
        else -> "Obese" to PastelCoral
    }

    // Healthy weight range (BMI 18.5 to 24.9)
    val minHealthyKg = 18.5 * (heightMeters * heightMeters)
    val maxHealthyKg = 24.9 * (heightMeters * heightMeters)

    val df = DecimalFormat("#,##0.0")

    Scaffold(
        topBar = {
            CommonTopBar(
                title = "BMI Calculator",
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
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Unit System Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                FilterChip(
                    selected = isMetric,
                    onClick = { isMetric = true },
                    label = { Text("Metric (cm / kg)") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = BrandPurple.copy(alpha = 0.2f),
                        selectedLabelColor = MaterialTheme.colorScheme.primary
                    )
                )
                FilterChip(
                    selected = !isMetric,
                    onClick = { isMetric = false },
                    label = { Text("Imperial (ft, in / lbs)") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = BrandPurple.copy(alpha = 0.2f),
                        selectedLabelColor = MaterialTheme.colorScheme.primary
                    )
                )
            }

            // Input Fields Card
            CustomCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Your Measurements",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    if (isMetric) {
                        OutlinedTextField(
                            value = heightCmStr,
                            onValueChange = { heightCmStr = it },
                            label = { Text("Height (cm)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.fillMaxWidth().testTag("bmi_height_cm"),
                            shape = RoundedCornerShape(14.dp)
                        )
                        OutlinedTextField(
                            value = weightKgStr,
                            onValueChange = { weightKgStr = it },
                            label = { Text("Weight (kg)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.fillMaxWidth().testTag("bmi_weight_kg"),
                            shape = RoundedCornerShape(14.dp)
                        )
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedTextField(
                                value = heightFtStr,
                                onValueChange = { heightFtStr = it },
                                label = { Text("Feet (ft)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(14.dp)
                            )
                            OutlinedTextField(
                                value = heightInStr,
                                onValueChange = { heightInStr = it },
                                label = { Text("Inches (in)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(14.dp)
                            )
                        }

                        OutlinedTextField(
                            value = weightLbsStr,
                            onValueChange = { weightLbsStr = it },
                            label = { Text("Weight (lbs)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp)
                        )
                    }
                }
            }

            if (bmi > 0) {
                // BMI Score Card
                CustomCard(
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = MaterialTheme.colorScheme.surface
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Your BMI Score",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = df.format(bmi),
                            style = MaterialTheme.typography.displayMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 42.sp
                            ),
                            color = categoryColor
                        )

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(categoryColor.copy(alpha = 0.2f))
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = category,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = categoryColor
                            )
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Visual BMI Gauge Bar
                        BmiGaugeBar(bmi = bmi)

                        Spacer(modifier = Modifier.height(16.dp))

                        if (heightMeters > 0) {
                            val healthyText = if (isMetric) {
                                "${df.format(minHealthyKg)} kg – ${df.format(maxHealthyKg)} kg"
                            } else {
                                val minLbs = minHealthyKg * 2.20462
                                val maxLbs = maxHealthyKg * 2.20462
                                "${df.format(minLbs)} lbs – ${df.format(maxLbs)} lbs"
                            }
                            Text(
                                text = "Healthy weight for your height: $healthyText",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Medical Disclaimer Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(BrandPurple.copy(alpha = 0.1f))
                    .padding(14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Medical Disclaimer: BMI is a general screening measure and does not directly calculate body fat or account for muscle mass, bone density, or age. It is not a substitute for clinical medical evaluation.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
fun BmiGaugeBar(bmi: Double) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(14.dp)
                .clip(RoundedCornerShape(7.dp))
        ) {
            Box(modifier = Modifier.weight(18.5f).background(PastelAmber))
            Box(modifier = Modifier.weight(6.4f).background(PastelGreen))
            Box(modifier = Modifier.weight(5f).background(PastelAmber))
            Box(modifier = Modifier.weight(10.1f).background(PastelCoral))
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("16.0", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("18.5", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("25.0", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("30.0", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("40.0", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
