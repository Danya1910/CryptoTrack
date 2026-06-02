package com.example.cryptotrack.presentation.widgets

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.cryptotrack.R
import com.example.cryptotrack.domain.model.MarketData
import com.example.cryptotrack.domain.util.MarketOrder
import com.example.cryptotrack.presentation.navigation.Screen
import com.example.cryptotrack.presentation.viewmodel.CoinGeckoViewModel
import com.example.cryptotrack.ui.theme.BlackBackground
import com.example.cryptotrack.ui.theme.DarkBlue
import com.example.cryptotrack.ui.theme.Green
import com.example.cryptotrack.ui.theme.Inter
import com.example.cryptotrack.ui.theme.OutlineGray
import com.example.cryptotrack.ui.theme.Red
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale


@Composable
fun CoinMarketWidget(
    order: MarketOrder,
    coins: List<MarketData>?,
    viewModel: CoinGeckoViewModel,
    navController: NavController,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
    ) {
        CoinMarketHat(
            order = order,
            viewModel = viewModel
        )
        CoinsMarketList(
            coins = coins,
            navController = navController,
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
private fun CoinMarketHat(
    order: MarketOrder,
    viewModel: CoinGeckoViewModel
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(25.dp)
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
        Text(
            text = "#",
            fontFamily = Inter,
            fontSize = 12.sp,
            fontWeight = FontWeight.Light,
            color = Color.Gray,
            modifier = Modifier.weight(0.2f),
        )
        Text(
            text = "Название",
            fontFamily = Inter,
            fontSize = 12.sp,
            fontWeight = FontWeight.Light,
            color = Color.Gray,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = "Цена",
            fontFamily = Inter,
            fontSize = 12.sp,
            fontWeight = FontWeight.Light,
            color = Color.Gray,
            modifier = Modifier.weight(0.8f),
        )
        Text(
            text = "24 часа",
            fontFamily = Inter,
            fontSize = 12.sp,
            fontWeight = FontWeight.Light,
            color = Color.Gray,
            modifier = Modifier
                .weight(0.6f)
                .clickable {
                    when (order) {
                        MarketOrder.DEFAULT -> {
                            viewModel.changeOrder(order = MarketOrder.PRICE_CHANGE_DESC)
                        }

                        MarketOrder.PRICE_CHANGE_DESC -> {
                            viewModel.changeOrder(order = MarketOrder.MARKET_CAP_ASC)
                        }

                        else -> {
                            viewModel.changeOrder(order = MarketOrder.DEFAULT)
                        }
                    }
                },
        )
        Text(
            text = "Market Cap",
            fontFamily = Inter,
            fontSize = 12.sp,
            fontWeight = FontWeight.Light,
            color = Color.Gray,
            modifier = Modifier
                .weight(0.8f)
        )
        //Spacer(modifier = Modifier.weight(0.2f))
    }
}

@SuppressLint("DefaultLocale")
@Composable
private fun CoinMarket(
    coin: MarketData?,
    navController: NavController,
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

    val symbols = DecimalFormatSymbols().apply {
        groupingSeparator = ' '
        decimalSeparator = '.'
    }

    val formatter = DecimalFormat("#,##0.00", symbols)

    val currentPriceFormatted = coin?.currentPrice.let {
        formatter.format(it)
    } ?: "0.00"

    val marketCapValue = coin?.marketCap ?: 0.0

    val marketCap = when {
        marketCapValue >= 1_000_000_000_000 -> {
            String.format(Locale.US, "%.2fT", marketCapValue / 1_000_000_000_000)
        }

        marketCapValue >= 1_000_000_000 -> {
            String.format(Locale.US, "%.2fB", marketCapValue / 1_000_000_000)
        }

        marketCapValue >= 1_000_000 -> {
            String.format(Locale.US, "%.2fM", marketCapValue / 1_000_000)
        }

        marketCapValue >= 1_000 -> {
            String.format(Locale.US, "%.2fK", marketCapValue / 1_000)
        }

        else -> {
            String.format(Locale.US, "%.0f", marketCapValue)
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable {
                navController.navigate(Screen.CoinDetails.createRoute(id = coin?.id ?: ""))
            }
            .padding(horizontal = 10.dp)
            .height(36.dp)
            .fillMaxWidth()
            .background(color = DarkBlue),
    ) {
        Text(
            text = coin?.marketCapRank.toString(),
            fontFamily = Inter,
            fontSize = 10.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(0.2f),
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(end = 10.dp)
                .fillMaxHeight()
                .weight(1f),
        ) {
            AsyncImage(
                model = coin?.image,
                contentDescription = null,
                modifier = Modifier.size(25.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = coin?.name ?: "Unknown",
                    fontFamily = Inter,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = coin?.symbol ?: "Unk",
                    fontFamily = Inter,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
        Text(
            textAlign = TextAlign.Left,
            text = "$$currentPriceFormatted",
            fontFamily = Inter,
            fontSize = 10.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(0.8f)
                .padding(end = 5.dp),
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.6f)
        ) {
            Icon(
                painter = painterResource(
                    if (isPositive) R.drawable.ic_up
                    else R.drawable.ic_down
                ),
                modifier = Modifier.size(7.dp),
                contentDescription = null,
                tint = percentageColor,
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = "$percentageUsd %",
                fontFamily = Inter,
                fontSize = 10.sp,
                fontWeight = FontWeight.Normal,
                color = percentageColor,
            )
        }
        Text(
            textAlign = TextAlign.Center,
            text = marketCap,
            fontFamily = Inter,
            fontSize = 10.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(0.6f)
        )
        Icon(
            painter = painterResource(R.drawable.ic_star),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .size(10.dp)
                .weight(0.2f)
        )

    }
}


@Composable
private fun CoinsMarketList(
    coins: List<MarketData>?,
    navController: NavController,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = DarkBlue,
                shape = RoundedCornerShape(10.dp)
            )

    ) {
        if (!coins.isNullOrEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
            ) {
                // Правильный способ инициализации элементов в LazyColumn
                items(
                    count = coins.size,
                    key = { index -> coins[index].id }
                ) { index ->
                    val coin = coins[index]
                    CoinMarket(
                        coin = coin,
                        navController = navController,
                    )
                    if (index != coins.size) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(
                                    color = OutlineGray
                                )
                        )
                    }
                }
            }
        }
    }

}