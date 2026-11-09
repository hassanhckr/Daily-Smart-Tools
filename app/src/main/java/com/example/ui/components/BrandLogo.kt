package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.R

/**
 * Official Daily Smart Tools Brand Logo:
 * Uses the exact uploaded 3D Toolbox Icon with smart utility tiles.
 */
@Composable
fun BrandLogo(
    modifier: Modifier = Modifier,
    size: Dp = 48.dp
) {
    val cornerRadius = size * 0.22f
    Box(
        modifier = modifier
            .size(size)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(cornerRadius),
                ambientColor = Color.Black.copy(alpha = 0.15f),
                spotColor = Color.Black.copy(alpha = 0.25f)
            )
            .clip(RoundedCornerShape(cornerRadius))
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.app_logo_asset),
            contentDescription = "Daily Smart Tools Logo",
            modifier = Modifier.size(size)
        )
    }
}
