package com.example.cryptotrack.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.cryptotrack.R
import com.example.cryptotrack.domain.model.FavoriteCoinDetails
import com.example.cryptotrack.domain.model.HistoryOfViewingCoin
import com.example.cryptotrack.presentation.navigation.Screen
import com.example.cryptotrack.presentation.util.price.formatPrice
import com.example.cryptotrack.presentation.viewmodel.CoinGeckoViewModel
import com.example.cryptotrack.presentation.viewmodel.CoinViewModel
import com.example.cryptotrack.presentation.widgets.BottomBar
import com.example.cryptotrack.ui.theme.BlackBackground
import com.example.cryptotrack.ui.theme.DarkBlue
import com.example.cryptotrack.ui.theme.Green
import com.example.cryptotrack.ui.theme.Inter
import com.example.cryptotrack.ui.theme.OutlineGray
import com.example.cryptotrack.ui.theme.Red
import com.example.cryptotrack.ui.theme.SearchBarColor
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols


@Composable
fun ProfileScreen(
    navController: NavController,
    coinViewModel: CoinViewModel,
    viewModel: CoinGeckoViewModel,
) {
    Scaffold(
        topBar = {},
        bottomBar = {
            BottomBar(
                navController = navController,
            )
        },
    ) { paddingValues ->
        Content(
            paddingValues = paddingValues,
            coinViewModel = coinViewModel,
            navController = navController,
            viewModel = viewModel,
        )
    }
}


@Composable
private fun Content(
    paddingValues: PaddingValues,
    coinViewModel: CoinViewModel,
    navController: NavController,
    viewModel: CoinGeckoViewModel,
) {

    val historyOfViewingList by coinViewModel.historyOfViewingCoins.collectAsState(initial = emptyList())
    val favoriteCoins by coinViewModel.favoriteCoins.collectAsState(initial = emptyList())

    LaunchedEffect(favoriteCoins) {
        if (favoriteCoins.isNotEmpty()) {
            val ids = favoriteCoins.joinToString(",") { it.id }
            viewModel.getFavoriteCoinsDetails(ids = ids)
        }
    }

    val favoriteCoinsDetails by viewModel.favoriteCoinsDetailsState.collectAsState()


    val details = remember(favoriteCoinsDetails.details) {
        favoriteCoinsDetails.details?.reversed().orEmpty()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = BlackBackground)
            .padding(horizontal = 15.dp)
            .padding(paddingValues)
    ) {
        UserInfo()
        UserStatsWidget()
        RecentlyViewed(coins = historyOfViewingList, navController = navController)
        FavoriteWidget(
            details = details,
            navController = navController
        )
    }

}

@Composable
private fun UserInfo() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .height(100.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_reddit),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = "Danya",
            fontSize = 28.sp,
            fontFamily = Inter,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun FavoriteWidget(
    details: List<FavoriteCoinDetails>?,
    navController: NavController,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 5.dp)
        ) {
            Text(
                text = "Избранные монеты",
                fontFamily = Inter,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Смотреть все",
                fontFamily = Inter,
                color = Green,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .clickable{
                        navController.navigate(Screen.Favorites.route)
                    }
            )
        }

        Spacer(modifier = Modifier.height(5.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = DarkBlue,
                    shape = RoundedCornerShape(10.dp)
                )
                .border(
                    width = 1.dp,
                    color = OutlineGray,
                    shape = RoundedCornerShape(10.dp)
                )
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                val coins = details?.take(4).orEmpty()

                coins.forEachIndexed { index, coinDetails ->

                    FavoriteItem(
                        coin = coinDetails,
                        navController = navController,
                    )

                    if (index != coins.lastIndex) {
                        Box(
                            modifier = Modifier
                                .height(1.dp)
                                .fillMaxWidth()
                                .background(color = OutlineGray)
                        )
                    }
                }
            }
        }

    }



}

@Composable
private fun FavoriteItem(
    coin: FavoriteCoinDetails,
    navController: NavController,
) {

    val price = formatPrice(value = coin.currentPrice)

    val symbols = DecimalFormatSymbols().apply {
        groupingSeparator = ' '
        decimalSeparator = '.'
    }

    val percentageColor = if (coin.priceChangePercentage24h >= 0) Green else Red

    val formatter = DecimalFormat("#,##0.00", symbols)

    val percentageText = coin.priceChangePercentage24h?.let {
        if (it > 0) {
            "+${formatter.format(it)}%"
        } else {
            "${formatter.format(it)}%"
        }
    } ?: "0.00%"

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp)
            .clickable{
                navController.navigate(Screen.CoinDetails.createRoute(id = coin?.id ?: ""))
            }
            .padding(horizontal = 10.dp)
    ) {
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
            text = "$$price",
            fontFamily = Inter,
            fontSize = 10.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        Text(
            textAlign = TextAlign.Right,
            text = percentageText,
            fontFamily = Inter,
            fontSize = 10.sp,
            fontWeight = FontWeight.Normal,
            color = percentageColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(0.3f)
        )

    }
}

@Composable
private fun UserStatsWidget() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .padding(start = 100.dp)
            .fillMaxWidth()
    ) {
        StatsItem(
            icon = R.drawable.ic_star,
            title = "Избранные",
            value = 12,
        )
        StatsItem(
            icon = R.drawable.ic_graph_up,
            title = "Отслеживается",
            value = 0,
        )
        StatsItem(
            icon = R.drawable.ic_history,
            title = "Просмотрено",
            value = 24,
        )
    }
}

@Composable
private fun StatsItem(
    icon: Int,
    title: String,
    value: Int,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(30.dp)
                .background(
                    color = SearchBarColor,
                    shape = RoundedCornerShape(5.dp)
                )
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(16.dp),
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            fontFamily = Inter,
            fontSize = 10.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White,
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = value.toString(),
            fontFamily = Inter,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
        )
    }
}

@Composable
private fun RecentlyViewed(
    coins: List<HistoryOfViewingCoin>,
    navController: NavController,
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Недавно просмотренные",
            fontFamily = Inter,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {
            coins.asReversed().forEach { coin ->
                RecentlyViewedItem(
                    coin = coin,
                    navController = navController,
                )
                Spacer(modifier = Modifier.width(10.dp))
            }
        }
    }
}

@Composable
private fun RecentlyViewedItem(
    coin: HistoryOfViewingCoin,
    navController: NavController,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .height(50.dp)
            .background(
                color = DarkBlue,
                shape = RoundedCornerShape(10.dp)
            )
            .border(
                width = 1.dp,
                color = OutlineGray,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable {
                navController.navigate(Screen.CoinDetails.createRoute(id = coin?.id ?: ""))
            }
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
        ) {
            AsyncImage(
                model = coin.imageUrl,
                contentDescription = null,
                modifier = Modifier.size(30.dp),
            )
            Spacer(
                modifier = Modifier
                    .width(8.dp)
            )
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxHeight()
            ) {
                Text(
                    text = coin.name,
                    fontFamily = Inter,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                )
                Spacer(
                    modifier = Modifier.height(5.dp)
                )
                Text(
                    text = "ETH",
                    fontFamily = Inter,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray,
                )
            }
        }
    }
}