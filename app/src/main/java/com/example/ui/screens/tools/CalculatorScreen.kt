package com.example.ui.screens.tools

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.CommonTopBar
import com.example.ui.theme.BrandAccentPink
import com.example.ui.theme.BrandPink
import com.example.ui.theme.BrandPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel,
    onBackClick: () -> Unit
) {
    BackHandler(onBack = onBackClick)

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showHistorySheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    Scaffold(
        topBar = {
            CommonTopBar(
                title = "Calculator",
                onBackClick = onBackClick,
                actions = {
                    IconButton(
                        onClick = { showHistorySheet = true },
                        modifier = Modifier.testTag("calc_history_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = "Calculation History",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Display Area
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.End
            ) {
                if (uiState.expressionText.isNotEmpty()) {
                    Text(
                        text = uiState.expressionText,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        fontSize = 18.sp,
                        textAlign = TextAlign.End,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }

                Text(
                    text = uiState.currentInput,
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = if (uiState.currentInput.length > 9) 34.sp else 44.sp
                    ),
                    color = if (uiState.error != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.End,
                    maxLines = 2,
                    modifier = Modifier.testTag("calc_display_text")
                )

                AnimatedVisibility(visible = uiState.error != null) {
                    Text(
                        text = uiState.error ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Keypad Grid
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Row 1: C, +/-, %, ÷
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CalcKey(text = "C", modifier = Modifier.weight(1f), colorType = KeyColorType.Function) { viewModel.onClear() }
                    CalcKey(text = "±", modifier = Modifier.weight(1f), colorType = KeyColorType.Function) { viewModel.onToggleSign() }
                    CalcKey(text = "%", modifier = Modifier.weight(1f), colorType = KeyColorType.Function) { viewModel.onPercentage() }
                    CalcKey(text = "÷", modifier = Modifier.weight(1f), colorType = KeyColorType.Operator) { viewModel.onOperation("/") }
                }

                // Row 2: 7, 8, 9, ×
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CalcKey(text = "7", modifier = Modifier.weight(1f)) { viewModel.onDigit("7") }
                    CalcKey(text = "8", modifier = Modifier.weight(1f)) { viewModel.onDigit("8") }
                    CalcKey(text = "9", modifier = Modifier.weight(1f)) { viewModel.onDigit("9") }
                    CalcKey(text = "×", modifier = Modifier.weight(1f), colorType = KeyColorType.Operator) { viewModel.onOperation("*") }
                }

                // Row 3: 4, 5, 6, -
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CalcKey(text = "4", modifier = Modifier.weight(1f)) { viewModel.onDigit("4") }
                    CalcKey(text = "5", modifier = Modifier.weight(1f)) { viewModel.onDigit("5") }
                    CalcKey(text = "6", modifier = Modifier.weight(1f)) { viewModel.onDigit("6") }
                    CalcKey(text = "−", modifier = Modifier.weight(1f), colorType = KeyColorType.Operator) { viewModel.onOperation("-") }
                }

                // Row 4: 1, 2, 3, +
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CalcKey(text = "1", modifier = Modifier.weight(1f)) { viewModel.onDigit("1") }
                    CalcKey(text = "2", modifier = Modifier.weight(1f)) { viewModel.onDigit("2") }
                    CalcKey(text = "3", modifier = Modifier.weight(1f)) { viewModel.onDigit("3") }
                    CalcKey(text = "+", modifier = Modifier.weight(1f), colorType = KeyColorType.Operator) { viewModel.onOperation("+") }
                }

                // Row 5: ., 0, ⌫, =
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CalcKey(text = ".", modifier = Modifier.weight(1f)) { viewModel.onDecimal() }
                    CalcKey(text = "0", modifier = Modifier.weight(1f)) { viewModel.onDigit("0") }
                    CalcKey(
                        icon = Icons.Default.Backspace,
                        modifier = Modifier.weight(1f),
                        colorType = KeyColorType.Function
                    ) { viewModel.onDelete() }
                    CalcKey(
                        text = "=",
                        modifier = Modifier.weight(1f),
                        colorType = KeyColorType.Equals
                    ) { viewModel.onEquals() }
                }
            }
        }
    }

    if (showHistorySheet) {
        ModalBottomSheet(
            onDismissRequest = { showHistorySheet = false },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Calculation History",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (uiState.history.isNotEmpty()) {
                        TextButton(onClick = { viewModel.clearHistory() }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Clear History",
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.size(4.dp))
                            Text("Clear", style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (uiState.history.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No recent calculations",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(uiState.history) { item ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = item.expression,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "= ${item.result}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

enum class KeyColorType {
    Normal,
    Function,
    Operator,
    Equals
}

@Composable
fun CalcKey(
    modifier: Modifier = Modifier,
    text: String? = null,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    colorType: KeyColorType = KeyColorType.Normal,
    onClick: () -> Unit
) {
    val bgColor = when (colorType) {
        KeyColorType.Normal -> MaterialTheme.colorScheme.surface
        KeyColorType.Function -> MaterialTheme.colorScheme.surfaceVariant
        KeyColorType.Operator -> BrandPurple.copy(alpha = 0.2f)
        KeyColorType.Equals -> BrandPurple
    }

    val contentColor = when (colorType) {
        KeyColorType.Normal -> MaterialTheme.colorScheme.onSurface
        KeyColorType.Function -> MaterialTheme.colorScheme.onSurfaceVariant
        KeyColorType.Operator -> MaterialTheme.colorScheme.primary
        KeyColorType.Equals -> Color.White
    }

    Box(
        modifier = modifier
            .height(64.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(bgColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (text != null) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = if (colorType == KeyColorType.Equals) FontWeight.Bold else FontWeight.Medium,
                    fontSize = 22.sp
                ),
                color = contentColor
            )
        } else if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
