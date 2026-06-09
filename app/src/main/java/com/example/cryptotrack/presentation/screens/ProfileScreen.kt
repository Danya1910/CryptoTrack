package com.example.cryptotrack.presentation.screens

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.cryptotrack.R
import com.example.cryptotrack.domain.model.FavoriteCoin
import com.example.cryptotrack.domain.model.FavoriteCoinDetails
import com.example.cryptotrack.domain.model.HistoryOfViewingCoin
import com.example.cryptotrack.domain.model.PurchaseCoin
import com.example.cryptotrack.presentation.navigation.Screen
import com.example.cryptotrack.presentation.util.price.formatPrice
import com.example.cryptotrack.presentation.util.uiModels.FavoriteUiItem
import com.example.cryptotrack.presentation.util.uiModels.Slice
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
    val purchase by coinViewModel.purchase.collectAsState(initial = emptyList())

    LaunchedEffect(favoriteCoins) {
        if (favoriteCoins.isNotEmpty()) {
            val ids = favoriteCoins.joinToString(",") { it.id }
            viewModel.getFavoriteCoinsDetails(ids = ids)
        }
    }

    val favoriteCoinsDetails by viewModel.favoriteCoinsDetailsState.collectAsState()


    val details = favoriteCoinsDetails.details?.reversed().orEmpty()

    val uiList: List<FavoriteUiItem> = if (favoriteCoinsDetails.details.isNullOrEmpty()) {
        favoriteCoins.map { FavoriteUiItem.Basic(it) }
    } else {
        details.map { FavoriteUiItem.Full(it) }
    }


    val favoritesCount = favoriteCoins.size
    val recentlyViewedCount = historyOfViewingList.size

    val parts = createSlices(purchases = purchase)


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = BlackBackground)
            .padding(horizontal = 15.dp)
            .padding(paddingValues)
    ) {
        UserInfo()
        UserStatsWidget(
            favoritesCount = favoritesCount,
            recentlyViewedCount = recentlyViewedCount,

        )
        Spacer(modifier = Modifier.height(10.dp))
        PurchaseWidget(
            parts = parts,
            navController = navController,
        )

        Spacer(modifier = Modifier.height(10.dp))
        FavoriteWidget(
            details = uiList,
            navController = navController,
        )
        Spacer(modifier = Modifier.height(10.dp))
        RecentlyViewed(coins = historyOfViewingList, navController = navController)
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
    details: List<FavoriteUiItem>?,
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
                textDecoration = TextDecoration.Underline,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .clickable {
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

                coins.forEachIndexed { index, item ->

                    when (item) {

                        is FavoriteUiItem.Full -> {
                            FavoriteItemFull(
                                coin = item.data,
                                navController = navController
                            )
                        }

                        is FavoriteUiItem.Basic -> {
                            FavoriteItemBasic(
                                coin = item.data,
                                navController = navController
                            )
                        }
                    }

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
private fun FavoriteItemFull(
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

    val percentageText = coin.priceChangePercentage24h.let {
        if (it > 0) {
            "+${formatter.format(it)}%"
        } else {
            "${formatter.format(it)}%"
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp)
            .clickable {
                navController.navigate(Screen.CoinDetails.createRoute(id = coin.id))
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
                model = coin.image,
                contentDescription = null,
                modifier = Modifier.size(25.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = coin.name,
                    fontFamily = Inter,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = coin.symbol,
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
private fun FavoriteItemBasic(
    coin: FavoriteCoin,
    navController: NavController,
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp)
            .clickable {
                navController.navigate(Screen.CoinDetails.createRoute(id = coin.id))
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
                model = coin.imageUrl,
                contentDescription = null,
                modifier = Modifier.size(25.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = coin.name,
                    fontFamily = Inter,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = coin.symbol,
                    fontFamily = Inter,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun UserStatsWidget(
    favoritesCount: Int,
    recentlyViewedCount: Int,
) {
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
            value = favoritesCount,
        )
        StatsItem(
            icon = R.drawable.ic_graph_up,
            title = "Отслеживается",
            value = 0,
        )
        StatsItem(
            icon = R.drawable.ic_history,
            title = "Просмотрено",
            value = recentlyViewedCount,
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
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White,
        )
        Spacer(modifier = Modifier.height(5.dp))
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
                Spacer(modifier = Modifier.width(5.dp))
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
                navController.navigate(Screen.CoinDetails.createRoute(id = coin.id))
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

@Composable
private fun PurchaseWidget(
    parts: List<Slice>,
    navController: NavController,
) {

    val total = formatPrice(parts.sumOf { it.value.toDouble() })

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
            .clickable{
                navController.navigate(Screen.Purchase.route)
            }
            .padding(all = 15.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .weight(0.6f)
            ) {
                Text(
                    text = "Стоимость портфеля",
                    fontFamily = Inter,
                    fontWeight = FontWeight.Normal,
                    fontSize = 10.sp,
                    color = Color.Gray,
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "$$total",
                    fontFamily = Inter,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    color = Color.White,
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "12.4% (24h)",
                    fontFamily = Inter,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = Green,
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "С 12 июл. 2006",
                    fontFamily = Inter,
                    fontWeight = FontWeight.Normal,
                    fontSize = 10.sp,
                    color = Color.Gray,
                )
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column(
                modifier = Modifier.weight(0.5f)
            ) {
                PieTest(slices = parts)
                Spacer(modifier = Modifier.height(10.dp))
                PurchasePercentage(parts = parts)
            }
        }
    }
}

@Composable
fun PieTest(
    slices: List<Slice>,

) {

    val gap = 6f

    Canvas(
        modifier = Modifier.size(50.dp)
    ) {

        val total = slices.sumOf { it.value.toDouble() }.toFloat()
        var startAngle = -90f
        val stroke = 10.dp.toPx()

        slices.forEach {

            val sweep = it.value / total * 360f

            drawArc(
                color = it.color,
                startAngle = startAngle + gap/2,
                sweepAngle = sweep - gap,
                useCenter = false,
                style = Stroke(width = stroke)
            )

            startAngle += sweep
        }
    }
}

@Composable

private fun PurchasePercentage(
    parts: List<Slice>,
) {
    val total = parts.sumOf { it.value.toDouble() }.toFloat()

    Column {
        parts.forEach { coin ->
            val percent = if (total > 0f) {
                coin.value / total * 100
            } else {
                0f
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
            ) {
                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .background(
                            color = coin.color,
                            shape = CircleShape
                        )
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = coin.name,
                    fontFamily = Inter,
                    fontWeight = FontWeight.Normal,
                    fontSize = 10.sp,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(0.8f),
                )
                Text(
                    text = "%.1f%%".format(percent).replace(',', '.'),
                    textAlign = TextAlign.End,
                    fontFamily = Inter,
                    fontWeight = FontWeight.Normal,
                    fontSize = 10.sp,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(0.3f),
                )
            }
        }
    }
}

fun createSlices(purchases: List<PurchaseCoin>): List<Slice> {

    val colors = listOf(
        Color(0xFF4F46E5),
        Color(0xFF22C55E),
        Color(0xFFF97316),
        Color(0xFFEAB308),
        Color(0xFF06B6D4),
        Color(0xFFEC4899)
    )

    // Объединяем одинаковые монеты и считаем их стоимость
    val grouped = purchases
        .groupBy { it.coinId }
        .map { (_, list) ->
            Slice(
                name = list.first().name,
                value = list.sumOf { it.amount * it.buyPrice }.toFloat(),
                color = Color.Transparent
            )
        }
        .sortedByDescending { it.value }

    // Оставляем 3 самые большие
    val result = mutableListOf<Slice>()

    grouped.take(3).forEachIndexed { index, slice ->
        result.add(
            slice.copy(
                color = colors[index % colors.size]
            )
        )
    }

    // Все остальные объединяем в "Другие"
    val otherValue = grouped
        .drop(3)
        .sumOf { it.value.toDouble() }
        .toFloat()

    if (otherValue > 0f) {
        result.add(
            Slice(
                name = "Другие",
                value = otherValue,
                color = Color(0xFFEF4444)
            )
        )
    }

    return result
}