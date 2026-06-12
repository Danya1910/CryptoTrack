package com.example.cryptotrack.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.cryptotrack.R
import com.example.cryptotrack.domain.model.FavoriteCoinDetails
import com.example.cryptotrack.domain.model.PurchaseCoin
import com.example.cryptotrack.presentation.navigation.Screen
import com.example.cryptotrack.presentation.util.price.aggregatePurchases
import com.example.cryptotrack.presentation.util.price.formatPrice
import com.example.cryptotrack.presentation.util.price.formatTimeAndDate
import com.example.cryptotrack.presentation.viewmodel.CoinGeckoViewModel
import com.example.cryptotrack.presentation.viewmodel.CoinViewModel
import com.example.cryptotrack.presentation.widgets.PurchaseHistoryTopAppBar
import com.example.cryptotrack.ui.theme.BlackBackground
import com.example.cryptotrack.ui.theme.DarkBlue
import com.example.cryptotrack.ui.theme.Green
import com.example.cryptotrack.ui.theme.Inter
import com.example.cryptotrack.ui.theme.OutlineGray
import com.example.cryptotrack.ui.theme.Purple
import com.example.cryptotrack.ui.theme.Red
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import kotlin.math.roundToInt


@Composable
fun PurchaseHistoryScreen(
    coinViewModel: CoinViewModel,
    viewModel: CoinGeckoViewModel,
    navController: NavController
) {
    Scaffold(
        topBar = {
            PurchaseHistoryTopAppBar(
                navController = navController,
            )
        }
    ) { paddingValues ->
        Content(
            paddingValues = paddingValues,
            coinViewModel = coinViewModel,
            viewModel = viewModel,
            navController = navController,
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun PurchaseHistoryScreenPreview() {

//    Scaffold(
//        topBar = {},
//    ) {paddingValues ->
//        Content(paddingValues = paddingValues)
//    }

}

@Composable
private fun Content(
    paddingValues: PaddingValues,
    coinViewModel: CoinViewModel,
    viewModel: CoinGeckoViewModel,
    navController: NavController,
) {

    val purchase by coinViewModel.purchase.collectAsState(initial = emptyList())
    val aggregatedPurchase = remember(purchase) {
        aggregatePurchases(purchase)
    }

    LaunchedEffect(purchase) {
        if (purchase.isNotEmpty()) {
            val ids = purchase.joinToString(",") { it.coinId }
            viewModel.getFavoriteCoinsDetails(ids = ids)
        }
    }

    val purchaseDetails by viewModel.favoriteCoinsDetailsState.collectAsState()

    val details = purchaseDetails.details

    val purchasesCount = purchase.size.toString()
    val investedSum = calculateInvested(purchases = purchase)

    val currentSum = calculateCurrentPrice(
        aggregatedPurchase = aggregatedPurchase,
        details = details
    )


    val currentFormatted = formatPrice(value = currentSum)
    val investedFormatted = formatPrice(value = investedSum)
    val profitPercentage = calculateProfitPercentage(
        current = currentSum,
        invested = investedSum,
    )


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = BlackBackground
            )
            .padding(paddingValues)
            .padding(horizontal = 15.dp)
    ) {
        PurchaseInfoHat(
            purchaseCount = purchasesCount,
            invested = "$investedFormatted$",
            currentPrice = "$currentFormatted$",
            profitPercentage = profitPercentage
        )
        Spacer(modifier = Modifier.height(10.dp))
        PurchasesList(
            purchase = purchase,
            details = details,
            coinViewModel = coinViewModel,
            navController = navController
        )
    }
}

@Composable
private fun PurchaseInfoHat(
    purchaseCount: String,
    invested: String,
    currentPrice: String,
    profitPercentage: Double,
) {

    val symbols = DecimalFormatSymbols().apply {
        groupingSeparator = ' '
        decimalSeparator = '.'
    }

    val percentageColor = if (profitPercentage >= 0) Green else Red

    val formatter = DecimalFormat("#,##0.00", symbols)

    val percentageText = profitPercentage.let {
        if (it > 0) {
            "+${formatter.format(it)}%"
        } else {
            "${formatter.format(it)}%"
        }
    }

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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = Purple,
                        shape = RoundedCornerShape(10.dp)
                    )
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_bag),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(0.8f)
            ) {
                Text(
                    text = "Всего покупок",
                    fontFamily = Inter,
                    color = Color.Gray,
                    fontWeight = FontWeight.Normal,
                    fontSize = 10.sp,
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = purchaseCount,
                    fontFamily = Inter,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                )
            }
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Инвестировано",
                    fontFamily = Inter,
                    color = Color.Gray,
                    fontWeight = FontWeight.Normal,
                    fontSize = 10.sp,
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = invested,
                    fontFamily = Inter,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                )

            }
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Прибыль",
                    fontFamily = Inter,
                    color = Color.Gray,
                    fontWeight = FontWeight.Normal,
                    fontSize = 10.sp,
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = currentPrice,
                    fontFamily = Inter,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = percentageText,
                    fontFamily = Inter,
                    color = percentageColor,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                )

            }
        }
    }
}


@Composable
private fun HistoryItem(
    purchase: PurchaseCoin,
    details: FavoriteCoinDetails,
    coinViewModel: CoinViewModel,
    navController: NavController,
) {

    val buyPrice = formatPrice(purchase.buyPrice)

    val firstInvestedDate = formatTimeAndDate(millis = purchase.buyDate)

    var offsetX by remember { mutableStateOf(0f) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(
                    color= Red
                )
                .clickable{
                    coinViewModel.deletePurchasedCoin(
                        id = purchase.id,
                        coinId = purchase.coinId,
                        name = purchase.name,
                        amount = purchase.amount,
                        buyPrice = purchase.buyPrice,
                        buyDate = purchase.buyDate,
                    )
                }
                .padding(end = 20.dp)
        ) {
            Text(
                text = "Удалить",
                fontFamily = Inter,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .offset {
                    IntOffset(offsetX.roundToInt(), 0)
                }
                .background(color = DarkBlue)
                .clickable {
                    navController.navigate(Screen.CoinDetails.createRoute(id = purchase.coinId))
                }
                .padding(horizontal = 10.dp, vertical = 5.dp)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { _, dragAmount ->
                            offsetX = (offsetX + dragAmount).coerceIn(-250f, 0f)
                        },
                        onDragEnd = {
                            offsetX = if (offsetX < -180)
                                -250f
                            else 0f
                        }
                    )
                },
        ) {
            AsyncImage(
                model = details.image,
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = purchase.name,
                    fontFamily = Inter,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "Количество",
                    fontFamily = Inter,
                    color = Color.Gray,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "${purchase.amount} ${details.symbol}",
                    fontFamily = Inter,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Цена за 1 монету",
                    fontFamily = Inter,
                    color = Color.Gray,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "$buyPrice$",
                    fontFamily = Inter,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = firstInvestedDate,
                        fontFamily = Inter,
                        color = Color.Gray,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Normal,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_right),
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }
        }

    }

}

@Composable
private fun PurchasesList(
    purchase: List<PurchaseCoin>,
    details: List<FavoriteCoinDetails>?,
    coinViewModel: CoinViewModel,
    navController: NavController,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(
                color = DarkBlue,
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(
                    rememberScrollState()
                )
        ) {
            purchase.forEachIndexed { index, item ->

                val coinDetails = details?.find { it.id == item.coinId }

                if (coinDetails != null) {
                    HistoryItem(
                        purchase = item,
                        details = coinDetails,
                        coinViewModel = coinViewModel,
                        navController = navController,
                    )

                    if (index != purchase.lastIndex) {
                        Box(
                            modifier = Modifier
                                .height(1.dp)
                                .fillMaxWidth()
                                .background(color = OutlineGray),
                        )
                    }
                }
            }
        }
    }
}