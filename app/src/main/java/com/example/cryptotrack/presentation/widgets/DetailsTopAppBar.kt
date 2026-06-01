package com.example.cryptotrack.presentation.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.cryptotrack.R
import com.example.cryptotrack.presentation.viewmodel.CoinViewModel
import com.example.cryptotrack.ui.theme.BlackBackground
import com.example.cryptotrack.ui.theme.Inter
import com.example.cryptotrack.ui.theme.Orange


@Composable
fun DetailsTopAppBar(
    navController: NavController,
    coinViewModel: CoinViewModel,
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(60.dp)
            .fillMaxWidth()
            .padding(
                start = 20.dp,
                end = 15.dp
            )
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_arrow_left),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.clickable {
                navController.popBackStack()
            }
        )
        Spacer(modifier = Modifier.width(25.dp))
        Text(
            text = "Coin Details",
            color = Color.White,
            fontFamily = Inter,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.weight(1f)
        )
        Icon(
            painter = painterResource(R.drawable.ic_star),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(22.dp)
        )
    }
}