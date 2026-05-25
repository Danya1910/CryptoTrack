package com.example.cryptotrack.presentation.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cryptotrack.R
import com.example.cryptotrack.ui.theme.BlackBackground
import com.example.cryptotrack.ui.theme.Inter
import com.example.cryptotrack.ui.theme.Orange


@Composable
fun TopAppBar(
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(color = BlackBackground)
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = "CryptoTrack",
            fontFamily = Inter,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Orange,
            modifier = Modifier.align(Alignment.Center)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_reddit),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(30.dp)
                    .offset(x = (-75).dp)
            )
        }
    }
}