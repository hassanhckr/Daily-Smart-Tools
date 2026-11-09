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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.PrimaryTabRow
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

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun PercentageCalculatorScreen(
    onBackClick: () -> Unit
) {
    BackHandler(onBack = onBackClick)

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("What is X% of Y", "X is what % of Y", "% Change", "Add/Subtract %")

    val df = DecimalFormat("#,##0.########")

    Scaffold(
        topBar = {
            CommonTopBar(
                title = "Percentage Calculator",
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
            // Tabs
            PrimaryTabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = title,
                                maxLines = 1,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal
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
                when (selectedTabIndex) {
                    0 -> ModeWhatIsXPercentOfY(df)
                    1 -> ModeXIsWhatPercentOfY(df)
                    2 -> ModePercentageChange(df)
                    3 -> ModeAddSubtractPercent(df)
                }
            }
        }
    }
}

@Composable
fun ModeWhatIsXPercentOfY(df: DecimalFormat) {
    var xStr by remember { mutableStateOf("15") }
    var yStr by remember { mutableStateOf("250") }

    val x = xStr.toDoubleOrNull() ?: 0.0
    val y = yStr.toDoubleOrNull() ?: 0.0
    val result = (x / 100.0) * y

    CustomCard(modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = "Calculate What is X% of Y",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            OutlinedTextField(
                value = xStr,
                onValueChange = { xStr = it },
                label = { Text("Percentage (X %)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth().testTag("pct_x_input"),
                shape = RoundedCornerShape(14.dp)
            )

            OutlinedTextField(
                value = yStr,
                onValueChange = { yStr = it },
                label = { Text("Of Number (Y)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth().testTag("pct_y_input"),
                shape = RoundedCornerShape(14.dp)
            )
        }
    }

    PercentResultBox(
        label = "$xStr% of $yStr =",
        value = df.format(result)
    )
}

@Composable
fun ModeXIsWhatPercentOfY(df: DecimalFormat) {
    var xStr by remember { mutableStateOf("50") }
    var yStr by remember { mutableStateOf("200") }

    val x = xStr.toDoubleOrNull() ?: 0.0
    val y = yStr.toDoubleOrNull() ?: 0.0
    val isZero = y == 0.0
    val result = if (isZero) 0.0 else (x / y) * 100.0

    CustomCard(modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = "Calculate X is what % of Y",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            OutlinedTextField(
                value = xStr,
                onValueChange = { xStr = it },
                label = { Text("Part (X)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            )

            OutlinedTextField(
                value = yStr,
                onValueChange = { yStr = it },
                label = { Text("Total (Y)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            )
        }
    }

    if (isZero) {
        Text("Denominator cannot be 0", color = MaterialTheme.colorScheme.error)
    } else {
        PercentResultBox(
            label = "$xStr is of $yStr =",
            value = "${df.format(result)}%"
        )
    }
}

@Composable
fun ModePercentageChange(df: DecimalFormat) {
    var fromStr by remember { mutableStateOf("80") }
    var toStr by remember { mutableStateOf("120") }

    val from = fromStr.toDoubleOrNull() ?: 0.0
    val to = toStr.toDoubleOrNull() ?: 0.0
    val isZero = from == 0.0
    val change = to - from
    val pctChange = if (isZero) 0.0 else (change / from) * 100.0

    CustomCard(modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = "Percentage Increase / Decrease",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            OutlinedTextField(
                value = fromStr,
                onValueChange = { fromStr = it },
                label = { Text("Initial Value (From)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            )

            OutlinedTextField(
                value = toStr,
                onValueChange = { toStr = it },
                label = { Text("Final Value (To)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            )
        }
    }

    if (isZero) {
        Text("Initial value cannot be 0", color = MaterialTheme.colorScheme.error)
    } else {
        val changeType = if (pctChange >= 0) "Increase" else "Decrease"
        PercentResultBox(
            label = "Percentage $changeType =",
            value = "${df.format(Math.abs(pctChange))}% (${if (pctChange >= 0) "+" else "-"}${df.format(Math.abs(change))})"
        )
    }
}

@Composable
fun ModeAddSubtractPercent(df: DecimalFormat) {
    var valStr by remember { mutableStateOf("500") }
    var pctStr by remember { mutableStateOf("10") }

    val base = valStr.toDoubleOrNull() ?: 0.0
    val pct = pctStr.toDoubleOrNull() ?: 0.0
    val delta = base * (pct / 100.0)
    val added = base + delta
    val subtracted = (base - delta).coerceAtLeast(0.0)

    CustomCard(modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = "Add / Subtract Percentage",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            OutlinedTextField(
                value = valStr,
                onValueChange = { valStr = it },
                label = { Text("Base Number") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            )

            OutlinedTextField(
                value = pctStr,
                onValueChange = { pctStr = it },
                label = { Text("Percentage (%)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            )
        }
    }

    PercentResultBox(
        label = "After adding $pctStr%:",
        value = df.format(added)
    )

    PercentResultBox(
        label = "After subtracting $pctStr%:",
        value = df.format(subtracted)
    )
}

@Composable
fun PercentResultBox(label: String, value: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(listOf(BrandPurple, BrandPink))
            )
            .padding(20.dp)
    ) {
        Column {
            Text(
                text = label,
                color = Color.White.copy(alpha = 0.85f),
                style = MaterialTheme.typography.labelMedium
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp
                ),
                color = Color.White
            )
        }
    }
}
