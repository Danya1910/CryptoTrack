package com.example.cryptotrack.presentation.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.cryptotrack.R
import com.example.cryptotrack.ui.theme.Inter

@Composable
fun PurchaseHistoryTopAppBar(
    navController: NavController,
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
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = "История покупок",
                color = Color.White,
                fontFamily = Inter,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
            )

        }
    }
}