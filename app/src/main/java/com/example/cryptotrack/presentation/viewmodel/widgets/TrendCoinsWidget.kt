package com.example.cryptotrack.presentation.viewmodel.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Colors
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage


@Composable
fun TrendCoinsWidget() {
}


@Composable
@Preview(showBackground = true)
private fun TrendCoinsWidgetPreview() {

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {

    }

}

@Composable
private fun CoinIcon() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = "1",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.width(5.dp))
            AsyncImage(
                model = "https://assets.coingecko.com/coins/images/28470/standard/MTLOGO.png?1696527464",
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = "Bitcoin",
                fontSize = 
            )
        }
    }
}