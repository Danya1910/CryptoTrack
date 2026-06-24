package com.example.cryptotrack.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cryptotrack.presentation.viewmodel.CoinGeckoViewModel
import com.example.cryptotrack.ui.theme.DarkBlue
import com.example.cryptotrack.ui.theme.Inter


@Composable
fun SplashScreen(
    viewModel: CoinGeckoViewModel,
    onMoveToMain: () -> Unit,
) {

    val isReady by viewModel.isSplashReady.collectAsStateWithLifecycle()

    LaunchedEffect(isReady) {
            if(isReady)
                onMoveToMain()
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = DarkBlue
            )
    ) {
        Text(
            text = "Crypto Track",
            fontFamily = Inter,
            fontWeight = FontWeight.SemiBold,
            fontSize = 28.sp,
            color = Color.White,
        )
    }

}