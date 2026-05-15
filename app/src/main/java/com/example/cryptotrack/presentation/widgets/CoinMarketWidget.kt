package com.example.cryptotrack.presentation.widgets

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.cryptotrack.R
import com.example.cryptotrack.domain.model.MarketData
import com.example.cryptotrack.ui.theme.BlackBackground
import com.example.cryptotrack.ui.theme.Green
import com.example.cryptotrack.ui.theme.Inter
import com.example.cryptotrack.ui.theme.Red


@Composable
fun CoinMarketWidget(
    coins: List<MarketData>?
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        CoinMarketHat()
        CoinsMarketList(
            coins = coins
        )
    }
}


@Composable
@Preview(showBackground = true, backgroundColor = 0xFF292929)
private fun CoinMarketWidgetPreview() {

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
//        CoinMarketHat()
//        Spacer(modifier = Modifier.height(5.dp))
//        CoinsMarketList()
    }
}

@Composable
fun CoinMarketHat(
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(25.dp)
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = "№",
            fontFamily = Inter,
            fontSize = 12.sp,
            fontWeight = FontWeight.Light,
            color = Color.Gray,
            modifier = Modifier.weight(0.3f),
        )
        Text(
            text = "Название",
            fontFamily = Inter,
            fontSize = 12.sp,
            fontWeight = FontWeight.Light,
            color = Color.Gray,
            modifier = Modifier.weight(2f),
        )
        Text(
            text = "Цена",
            fontFamily = Inter,
            fontSize = 12.sp,
            fontWeight = FontWeight.Light,
            color = Color.Gray,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = "24 часа",
            fontFamily = Inter,
            fontSize = 12.sp,
            fontWeight = FontWeight.Light,
            color = Color.Gray,
            modifier = Modifier.weight(0.7f),
        )
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun CoinMarket(
    coin: MarketData?
) {

    val isPositive =
        (coin?.priceChangePercentage24h ?: 0.0) >= 0

    val percentageColor =
        if (isPositive) Green
        else Red

    val percentageUsd = String.format(
        "%.1f",
        kotlin.math.abs(coin?.priceChangePercentage24h ?: 0.0)
    )

    val currentPrice = coin?.currentPrice

    val currentPriceUsd = when {
        currentPrice!! < 0.01 -> String.format("%.6f", currentPrice)  // очень маленькие цены
        currentPrice < 10.0 -> String.format("%.4f", currentPrice)   // меньше 1 - 4 знака
        currentPrice < 100.0 -> String.format("%.2f", currentPrice)  // 1-10 - 2 знака
        else -> String.format("%.1f", currentPrice)          // больше 10 - без знаков
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable {
                //переход на стариницу моенты
            }
            .padding(horizontal = 10.dp)
            .height(30.dp)
            .fillMaxWidth(),
    ) {
        Text(
            text = coin?.marketCapRank.toString(),
            fontFamily = Inter,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(0.3f),
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(end = 10.dp)
                .fillMaxHeight()
                .weight(2f),
        ) {
            AsyncImage(
                model = coin?.image,
                contentDescription = null,
                modifier = Modifier.size(25.dp),
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = coin?.name ?: "Unknown",
                fontFamily = Inter,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = coin?.symbol ?: "Unk",
                fontFamily = Inter,
                fontSize = 14.sp,
                fontWeight = FontWeight.Light,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Text(
            textAlign = TextAlign.Left,
            text = "$currentPriceUsd $",
            fontFamily = Inter,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f)
                .padding(end = 5.dp),
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.7f)
        ) {
            Text(
                text = "$percentageUsd %",
                fontFamily = Inter,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = percentageColor,
            )
            Spacer(modifier = Modifier.width(3.dp))
            Icon(
                painter = painterResource(
                    if (isPositive) R.drawable.ic_up
                    else R.drawable.ic_down
                ),
                contentDescription = null,
                tint = percentageColor,
            )
        }
    }
}


@Composable
fun CoinsMarketList(
    coins: List<MarketData>?
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                shape = RoundedCornerShape(30.dp),
                elevation = 4.dp,
                spotColor = Color.White,
            )
            .background(
                color = BlackBackground,
                shape = RoundedCornerShape(30.dp)
            )
            .padding(
                horizontal = 10.dp,
                vertical = 15.dp
            )
    ) {
        Column {
            coins?.forEach { coin ->
                CoinMarket(coin = coin)
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}