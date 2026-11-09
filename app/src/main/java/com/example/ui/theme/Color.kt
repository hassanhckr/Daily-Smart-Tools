package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Brand Colors requested:
// Primary Pink: #E9B8D5
// Primary Purple: #A77BD1
// Light Background: #FFF9FD
// Surface: #FFFFFF
// Dark Background: #17131E
// Dark Surface: #251D2E
// Dark Purple: #C7A1EE
// Accent Pink: #F0B9D8

val BrandPink = Color(0xFFE9B8D5)
val BrandPurple = Color(0xFFA77BD1)
val BrandAccentPink = Color(0xFFF0B9D8)
val BrandDarkPurple = Color(0xFFC7A1EE)

// Light Theme Palette
val LightBackground = Color(0xFFFFF9FD)
val LightSurface = Color(0xFFFFFFFF)
val LightSurfaceVariant = Color(0xFFF8F0F9)
val LightPrimary = Color(0xFF9663C2)
val LightOnPrimary = Color(0xFFFFFFFF)
val LightPrimaryContainer = Color(0xFFF4E8FC)
val LightOnPrimaryContainer = Color(0xFF371252)
val LightSecondary = Color(0xFFD47CA8)
val LightOnSecondary = Color(0xFFFFFFFF)
val LightSecondaryContainer = Color(0xFFFDF0F6)
val LightOnSecondaryContainer = Color(0xFF4A1033)
val LightTertiary = Color(0xFF7A68A6)
val LightOnBackground = Color(0xFF221A26)
val LightOnSurface = Color(0xFF221A26)
val LightOnSurfaceVariant = Color(0xFF5B5060)
val LightOutline = Color(0xFFE8DBEC)
val LightOutlineVariant = Color(0xFFF1E6F3)

// Dark Theme Palette
val DarkBackground = Color(0xFF17131E)
val DarkSurface = Color(0xFF251D2E)
val DarkSurfaceVariant = Color(0xFF33293D)
val DarkPrimary = Color(0xFFC7A1EE)
val DarkOnPrimary = Color(0xFF351253)
val DarkPrimaryContainer = Color(0xFF522879)
val DarkOnPrimaryContainer = Color(0xFFF4E8FC)
val DarkSecondary = Color(0xFFF0B9D8)
val DarkOnSecondary = Color(0xFF521639)
val DarkSecondaryContainer = Color(0xFF6A244E)
val DarkOnSecondaryContainer = Color(0xFFFDF0F6)
val DarkTertiary = Color(0xFFD6C6F7)
val DarkOnBackground = Color(0xFFF0E8F4)
val DarkOnSurface = Color(0xFFF0E8F4)
val DarkOnSurfaceVariant = Color(0xFFD5C9DB)
val DarkOutline = Color(0xFF4C3E56)
val DarkOutlineVariant = Color(0xFF382D42)

// Category & Status Colors
val PastelGreen = Color(0xFF88C999)
val PastelCoral = Color(0xFFF4988F)
val PastelBlue = Color(0xFF8BB7F0)
val PastelAmber = Color(0xFFF7C268)
val PastelLavender = Color(0xFFBCA6EA)

// Gradient Brushes
val BrandGradient = Brush.linearGradient(
    colors = listOf(BrandPurple, BrandPink)
)

val BrandDarkGradient = Brush.linearGradient(
    colors = listOf(BrandDarkPurple, BrandAccentPink)
)

val CardGlowLight = Brush.verticalGradient(
    colors = listOf(Color(0xFFFFFFFF), Color(0xFFFCF5FD))
)

val CardGlowDark = Brush.verticalGradient(
    colors = listOf(Color(0xFF2B2236), Color(0xFF231B2C))
)
