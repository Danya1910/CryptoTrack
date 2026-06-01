package com.example.cryptotrack.presentation.widgets

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.cryptotrack.R
import com.example.cryptotrack.domain.model.TrendCoin
import com.example.cryptotrack.domain.model.TrendCoins
import com.example.cryptotrack.presentation.navigation.Screen
import com.example.cryptotrack.presentation.util.price.formatPrice
import com.example.cryptotrack.ui.theme.DarkBlue
import com.example.cryptotrack.ui.theme.DarkGray
import com.example.cryptotrack.ui.theme.Green
import com.example.cryptotrack.ui.theme.Inter
import com.example.cryptotrack.ui.theme.OutlineGray
import com.example.cryptotrack.ui.theme.Red



@Composable
fun TrendWidget(
    trends: TrendCoins?,
    navController: NavController,
) {
    val coins = trends?.coins
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "Trending coins",
            fontFamily = Inter,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White,
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {
            coins?.forEachIndexed { index, coin ->
                TrendItem(
                    coin = coin,
                    navController = navController,
                )
                if(index != coins.lastIndex){
                    Spacer(modifier = Modifier.width(5.dp))
                }
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
private fun TrendItem(
    coin: TrendCoin?,
    navController: NavController,
) {

    val isPositive =
        (coin?.data?.priceChangePercentage24h?.usd ?: 0.0) >= 0.0

    val percentageColor =
        if (isPositive) Green
        else Red

    val percentageUsd = String.format(
        "%.1f",
        kotlin.math.abs(coin?.data?.priceChangePercentage24h?.usd ?: 0.0)
    )

    val currentPriceFormatted = formatPrice(coin?.data?.price)

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .width(100.dp)
            .background(
                color = DarkBlue,
                shape = RoundedCornerShape(10.dp)
            )
            .border(
                color = OutlineGray,
                width = 1.dp,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable{
                navController.navigate(Screen.CoinDetails.createRoute(id = coin?.id.toString()))
            }
            .padding(all = 8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(16.dp)
                        .background(
                            color = DarkGray,
                            shape = CircleShape
                        )
                ) {
                    Text(
                        text = coin?.marketCapRank.toString(),
                        fontFamily = Inter,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = painterResource(R.drawable.ic_fire),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
            }
            AsyncImage(
                model = coin?.thumb,
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .clip(shape = CircleShape),
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = coin?.name.toString(),
                fontFamily = Inter,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = coin?.symbol.toString(),
                fontFamily = Inter,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "$$currentPriceFormatted",
                fontFamily = Inter,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(5.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "$percentageUsd %",
                    fontFamily = Inter,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Normal,
                    color = percentageColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.width(3.dp))
                Icon(
                    painter = painterResource(
                        if (isPositive) R.drawable.ic_up
                        else R.drawable.ic_down
                    ),
                    contentDescription = null,
                    tint = percentageColor,
                    modifier = Modifier.size(7.dp)
                )
            }
        }
    }
}