package com.example.ui.screens.tools

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CommonTopBar
import com.example.ui.components.CustomCard
import com.example.ui.theme.BrandPink
import com.example.ui.theme.BrandPurple
import com.example.ui.theme.PastelAmber
import com.example.ui.theme.PastelGreen
import java.security.SecureRandom

@Composable
fun PasswordGeneratorScreen(
    onBackClick: () -> Unit
) {
    BackHandler(onBack = onBackClick)

    val context = LocalContext.current

    var length by remember { mutableFloatStateOf(16f) }
    var includeUppercase by remember { mutableStateOf(true) }
    var includeLowercase by remember { mutableStateOf(true) }
    var includeNumbers by remember { mutableStateOf(true) }
    var includeSymbols by remember { mutableStateOf(true) }
    var excludeConfusing by remember { mutableStateOf(false) }

    fun generate(): String {
        return generateSecurePassword(
            length = length.toInt(),
            upper = includeUppercase,
            lower = includeLowercase,
            numbers = includeNumbers,
            symbols = includeSymbols,
            excludeConfusing = excludeConfusing
        )
    }

    var password by remember { mutableStateOf(generate()) }

    val strength = evaluateStrength(password)

    Scaffold(
        topBar = {
            CommonTopBar(
                title = "Password Generator",
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
            // Generated Password Display Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(listOf(BrandPurple, BrandPink))
                    )
                    .padding(22.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Generated Password",
                            color = Color.White.copy(alpha = 0.85f),
                            style = MaterialTheme.typography.labelMedium
                        )

                        IconButton(
                            onClick = { password = generate() },
                            modifier = Modifier.size(36.dp).testTag("pwd_regenerate_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Regenerate",
                                tint = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = password,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            letterSpacing = 1.sp
                        ),
                        color = Color.White,
                        modifier = Modifier.testTag("pwd_display_text")
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Strength bar
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Strength: ${strength.label}",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White.copy(alpha = 0.9f),
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "${password.length} characters",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { strength.score },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = strength.color,
                            trackColor = Color.White.copy(alpha = 0.3f)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Password", password)
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "Password copied to clipboard", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth().testTag("pwd_copy_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = null,
                            tint = BrandPurple,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Copy to Clipboard",
                            color = BrandPurple,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Options Card
            CustomCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Customize Generator",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    // Length Slider
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Length", style = MaterialTheme.typography.bodyMedium)
                            Text("${length.toInt()}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }
                        Slider(
                            value = length,
                            onValueChange = {
                                length = it
                                password = generate()
                            },
                            valueRange = 6f..40f,
                            steps = 33,
                            colors = SliderDefaults.colors(
                                thumbColor = MaterialTheme.colorScheme.primary,
                                activeTrackColor = MaterialTheme.colorScheme.primary
                            )
                        )
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

                    PasswordOptionSwitch(
                        label = "Uppercase Letters (A-Z)",
                        checked = includeUppercase,
                        onCheckedChange = {
                            if (includeUppercase && !includeLowercase && !includeNumbers && !includeSymbols) return@PasswordOptionSwitch
                            includeUppercase = it
                            password = generate()
                        }
                    )

                    PasswordOptionSwitch(
                        label = "Lowercase Letters (a-z)",
                        checked = includeLowercase,
                        onCheckedChange = {
                            if (!includeUppercase && includeLowercase && !includeNumbers && !includeSymbols) return@PasswordOptionSwitch
                            includeLowercase = it
                            password = generate()
                        }
                    )

                    PasswordOptionSwitch(
                        label = "Numbers (0-9)",
                        checked = includeNumbers,
                        onCheckedChange = {
                            if (!includeUppercase && !includeLowercase && includeNumbers && !includeSymbols) return@PasswordOptionSwitch
                            includeNumbers = it
                            password = generate()
                        }
                    )

                    PasswordOptionSwitch(
                        label = "Symbols (!@#$)",
                        checked = includeSymbols,
                        onCheckedChange = {
                            if (!includeUppercase && !includeLowercase && !includeNumbers && includeSymbols) return@PasswordOptionSwitch
                            includeSymbols = it
                            password = generate()
                        }
                    )

                    PasswordOptionSwitch(
                        label = "Exclude Confusing Characters (1, l, I, 0, O)",
                        checked = excludeConfusing,
                        onCheckedChange = {
                            excludeConfusing = it
                            password = generate()
                        }
                    )
                }
            }

            // Security Notice
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(BrandPurple.copy(alpha = 0.08f))
                    .padding(14.dp)
            ) {
                Text(
                    text = "Security Notice: Passwords are created using Android's cryptographically secure SecureRandom. Generated passwords are never saved to storage or transmitted over the network.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
fun PasswordOptionSwitch(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = BrandPurple
            )
        )
    }
}

data class PasswordStrength(
    val label: String,
    val score: Float,
    val color: Color
)

fun evaluateStrength(password: String): PasswordStrength {
    if (password.length < 8) return PasswordStrength("Weak", 0.25f, Color(0xFFE57373))

    var score = 0
    if (password.any { it.isUpperCase() }) score++
    if (password.any { it.isLowerCase() }) score++
    if (password.any { it.isDigit() }) score++
    if (password.any { "!@#$%^&*()_+-=[]{}|;:,.<>?".contains(it) }) score++
    if (password.length >= 14) score++

    return when {
        score <= 2 -> PasswordStrength("Fair", 0.45f, PastelAmber)
        score <= 4 -> PasswordStrength("Strong", 0.75f, BrandPurple)
        else -> PasswordStrength("Very Strong", 1.0f, PastelGreen)
    }
}

fun generateSecurePassword(
    length: Int,
    upper: Boolean,
    lower: Boolean,
    numbers: Boolean,
    symbols: Boolean,
    excludeConfusing: Boolean
): String {
    val upperChars = if (excludeConfusing) "ABCDEFGHJKLMNPQRSTUVWXYZ" else "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    val lowerChars = if (excludeConfusing) "abcdefghijkmnopqrstuvwxyz" else "abcdefghijklmnopqrstuvwxyz"
    val numberChars = if (excludeConfusing) "23456789" else "0123456789"
    val symbolChars = "!@#$%^&*()_+-=[]{}|;:,.<>?"

    var pool = ""
    if (upper) pool += upperChars
    if (lower) pool += lowerChars
    if (numbers) pool += numberChars
    if (symbols) pool += symbolChars

    if (pool.isEmpty()) pool = lowerChars

    val random = SecureRandom()
    val sb = StringBuilder()
    for (i in 0 until length) {
        val index = random.nextInt(pool.length)
        sb.append(pool[index])
    }
    return sb.toString()
}
