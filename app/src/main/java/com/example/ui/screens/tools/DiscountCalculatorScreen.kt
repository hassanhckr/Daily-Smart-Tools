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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CommonTopBar
import com.example.ui.components.CustomCard
import com.example.ui.theme.BrandAccentPink
import com.example.ui.theme.BrandPink
import com.example.ui.theme.BrandPurple
import java.text.DecimalFormat

@Composable
fun DiscountCalculatorScreen(
    currencyCode: String = "PKR",
    onBackClick: () -> Unit
) {
    BackHandler(onBack = onBackClick)

    var originalPriceStr by remember { mutableStateOf("1000") }
    var discountPercentStr by remember { mutableStateOf("20") }
    var taxPercentStr by remember { mutableStateOf("0") }

    val originalPrice = originalPriceStr.toDoubleOrNull() ?: 0.0
    val discountPercent = discountPercentStr.toDoubleOrNull() ?: 0.0
    val taxPercent = taxPercentStr.toDoubleOrNull() ?: 0.0

    val discountAmount = (originalPrice * (discountPercent / 100.0)).coerceAtLeast(0.0)
    val priceAfterDiscount = (originalPrice - discountAmount).coerceAtLeast(0.0)
    val taxAmount = (priceAfterDiscount * (taxPercent / 100.0)).coerceAtLeast(0.0)
    val finalPrice = priceAfterDiscount + taxAmount

    val df = DecimalFormat("#,##0.00")

    Scaffold(
        topBar = {
            CommonTopBar(
                title = "Discount Calculator",
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
            // Inputs Card
            CustomCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text(
                        text = "Price & Discount Details",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    OutlinedTextField(
                        value = originalPriceStr,
                        onValueChange = { originalPriceStr = it },
                        label = { Text("Original Price ($currencyCode)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("discount_price_input"),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )

                    OutlinedTextField(
                        value = discountPercentStr,
                        onValueChange = { discountPercentStr = it },
                        label = { Text("Discount Percentage (%)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("discount_percent_input"),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )

                    // Quick percentage chips
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("10", "15", "20", "25", "50").forEach { pct ->
                            FilterChip(
                                selected = discountPercentStr == pct,
                                onClick = { discountPercentStr = pct },
                                label = { Text("$pct%") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = BrandPurple.copy(alpha = 0.2f),
                                    selectedLabelColor = MaterialTheme.colorScheme.primary
                                )
                            )
                        }
                    }

                    OutlinedTextField(
                        value = taxPercentStr,
                        onValueChange = { taxPercentStr = it },
                        label = { Text("Tax / VAT % (Optional)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )
                }
            }

            // Results Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(BrandPurple, BrandPink)
                        )
                    )
                    .padding(22.dp)
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalOffer,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "Final Payable Price",
                            color = Color.White.copy(alpha = 0.9f),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "$currencyCode ${df.format(finalPrice)}",
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 36.sp
                        ),
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = Color.White.copy(alpha = 0.3f))
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Savings,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.size(4.dp))
                                Text(
                                    text = "You Save",
                                    color = Color.White.copy(alpha = 0.85f),
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                            Text(
                                text = "$currencyCode ${df.format(discountAmount)}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        if (taxAmount > 0) {
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "Tax Included",
                                    color = Color.White.copy(alpha = 0.85f),
                                    style = MaterialTheme.typography.labelMedium
                                )
                                Text(
                                    text = "+$currencyCode ${df.format(taxAmount)}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
