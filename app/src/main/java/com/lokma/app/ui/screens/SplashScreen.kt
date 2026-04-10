package com.lokma.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lokma.app.R

@Composable
fun SplashScreen() {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(color = SplashBackground),
        contentAlignment = Alignment.Center
    ) {
        val logoSize = (maxWidth * 0.5f).coerceIn(120.dp, 180.dp)

        Image(
            painter = painterResource(id = R.drawable.lokma_logo),
            contentDescription = "Lokma logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .size(logoSize)
        )
    }
}

private val SplashBackground = androidx.compose.ui.graphics.Color(0xFFEAF7EE)
