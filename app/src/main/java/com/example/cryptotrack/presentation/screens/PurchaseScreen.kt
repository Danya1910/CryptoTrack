package com.example.cryptotrack.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.modifier.modifierLocalConsumer
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
import com.example.cryptotrack.domain.model.PurchaseCoin
import com.example.cryptotrack.presentation.navigation.Screen
import com.example.cryptotrack.presentation.util.price.aggregatePurchases
import com.example.cryptotrack.presentation.util.price.formatRate
import com.example.cryptotrack.presentation.util.price.getCoinPlural
import com.example.cryptotrack.presentation.util.uiModels.AggregatedPurchase
import com.example.cryptotrack.presentation.viewmodel.CoinGeckoViewModel
import com.example.cryptotrack.presentation.viewmodel.CoinViewModel
import com.example.cryptotrack.presentation.widgets.BottomBar
import com.example.cryptotrack.presentation.widgets.PurchaseTopAppBar
import com.example.cryptotrack.presentation.widgets.SkeletonBox
import com.example.cryptotrack.ui.theme.BlackBackground
import com.example.cryptotrack.ui.theme.DarkBlue
import com.example.cryptotrack.ui.theme.Green
import com.example.cryptotrack.ui.theme.Inter
import com.example.cryptotrack.ui.theme.Lavender
import com.example.cryptotrack.ui.theme.OutlineGray
import com.example.cryptotrack.ui.theme.Purple
import com.example.cryptotrack.ui.theme.Red
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols


@Composable
fun PurchaseScreen(
    navController: NavController,
    coinViewModel: CoinViewModel,
    viewModel: CoinGeckoViewModel,
) {
    Scaffold(
        topBar = {
            PurchaseTopAppBar(
                navController = navController,
            )
        },
        bottomBar = {
            BottomBar(
                navController = navController
            )
        },
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

    var isAscending by remember { mutableStateOf(true) }

    val sortedPurchases = remember(aggregatedPurchase, isAscending) {
        sortPurchasesByPrice(
            purchases = aggregatedPurchase,
            isAscending = isAscending
        )
    }

    LaunchedEffect(aggregatedPurchase) {
        if (purchase.isNotEmpty()) {
            val ids = aggregatedPurchase.joinToString(",") { it.coinId }
            viewModel.getFavoriteCoinsDetails(ids = ids)
        }
    }

    val purchaseDetails by viewModel.favoriteCoinsDetailsState.collectAsState()

    val details = purchaseDetails.details

    val isApiDataAvailable = !details.isNullOrEmpty()

    val investedSum = calculateInvested(purchases = purchase)

    val currentSum = if (isApiDataAvailable) {
        calculateCurrentPrice(
            aggregatedPurchase = aggregatedPurchase,
            details = details
        )
    } else null

    val profit = if (currentSum != null) currentSum - investedSum
    else null

    val investedFormatted = formatRate(value = investedSum)
    val currentFormatted = currentSum?.let { formatRate(value = it) }


    val profitPercentage = if (isApiDataAvailable && currentSum != null) calculateProfitPercentage(
        current = currentSum,
        invested = investedSum,
    ) else null

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = BlackBackground
            )
            .padding(paddingValues)
            .padding(horizontal = 15.dp)
            .padding(bottom = 10.dp)
    ) {
        ConclusionWidget(
            invested = investedFormatted,
            profit = profit,
            investedSum = investedSum,
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 15.dp)
            .padding(vertical = 10.dp)
            .padding(bottom = 75.dp)
    ) {
        TotalVolume(
            profitPercentage = profitPercentage,
            currentPrice = currentFormatted,
            investedSum = investedSum,
        )

        Spacer(modifier = Modifier.height(10.dp))

        HistoryButton(navController = navController)

        Spacer(modifier = Modifier.height(10.dp))

        AddPurchaseButton(navController = navController)

        Spacer(modifier = Modifier.height(10.dp))

        ListHat(
            isAscending = isAscending,
            onClick = {
                isAscending = !isAscending
            }
        )

        Spacer(modifier = Modifier.height(5.dp))
        if (purchase.isEmpty())
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                HelpWidget(
                    navController = navController,
                )
            }
        else
            CoinsList(
                purchase = sortedPurchases,
                details = details,
            )

    }
}

@Composable
private fun TotalVolume(
    profitPercentage: Double?,
    currentPrice: String?,
    investedSum: Double,
) {
    val symbols = DecimalFormatSymbols().apply {
        groupingSeparator = ' '
        decimalSeparator = '.'
    }

    val percentageColor = when {
        profitPercentage == null -> Color.Gray
        profitPercentage >= 0 -> Green
        else -> Red
    }

    val formatter = DecimalFormat("#,##0.00", symbols)

    val percentageText = profitPercentage?.let {
        if (it > 0) "+${formatter.format(it)}%" else "${formatter.format(it)}%"
    } ?: "--%"

    val displayCurrentPrice = currentPrice ?: "--"

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
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 15.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
            ) {
                Text(
                    text = "Общая стоимость",
                    fontFamily = Inter,
                    color = Color.Gray,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                )
                Spacer(modifier = Modifier.height(10.dp))
                if(investedSum == 0.0) {
                    Text(
                        text = "0.0 $",
                        fontFamily = Inter,
                        color = Color.White,
                        fontWeight = FontWeight.Normal,
                        fontSize = 20.sp,
                    )
                } else {
                    if(currentPrice.isNullOrEmpty()){
                        SkeletonBox(
                            modifier = Modifier
                                .height(22.dp)
                                .width(70.dp)
                        )
                    } else {
                        Text(
                            text = "$displayCurrentPrice $",
                            fontFamily = Inter,
                            color = Color.White,
                            fontWeight = FontWeight.Normal,
                            fontSize = 20.sp,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (investedSum == 0.0) {
                        Text(
                            text = "0.0%",
                            fontFamily = Inter,
                            color = percentageColor,
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp,
                        )
                    } else {
                        if(currentPrice.isNullOrEmpty()){
                            SkeletonBox(
                                modifier = Modifier
                                    .height(14.dp)
                                    .width(30.dp)
                            )
                        } else {
                            Text(
                                text = percentageText,
                                fontFamily = Inter,
                                color = percentageColor,
                                fontWeight = FontWeight.Normal,
                                fontSize = 12.sp,
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "за все время",
                        fontFamily = Inter,
                        color = Color.White,
                        fontWeight = FontWeight.Normal,
                        fontSize = 10.sp,
                    )
                }
            }
        }
    }
}

@Composable
private fun HistoryButton(
    navController: NavController,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
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
                navController.navigate(Screen.PurchaseHistory.route)
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_clock),
                contentDescription = null,
                tint = Lavender,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "История покупок",
                fontFamily = Inter,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
            )
        }
    }
}

@Composable
private fun AddPurchaseButton(
    navController: NavController,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
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
                navController.navigate(Screen.AddPurchase.route)
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_plus),
                contentDescription = null,
                tint = Lavender,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Добавить покупку",
                fontFamily = Inter,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
            )
        }
    }
}

@Composable
private fun ListItem(
    purchase: AggregatedPurchase,
    details: FavoriteCoinDetails?,
) {

    val totalPrice = formatRate(purchase.totalValue)
    val percentage = details?.priceChangePercentage24h?.let { "%.2f".format(it) } ?: "--"

    val percentageColor = details?.priceChangePercentage24h?.let { if (it >= 0.0) Green else Red }

    val insteadOfSymbol = if (details?.symbol.isNullOrEmpty()) {
        getCoinPlural(purchase.totalAmount)
    } else {
        details.symbol.uppercase()
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(55.dp)
            .fillMaxWidth()
            .background(color = DarkBlue)
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(0.7f)
        ) {
            AsyncImage(
                model = purchase.imageUrl,
                contentDescription = null,
                modifier = Modifier.size(30.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = purchase.name,
                    fontFamily = Inter,
                    color = Color.White,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "${purchase.totalAmount} $insteadOfSymbol",
                    fontFamily = Inter,
                    color = Color.Gray,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                )
            }
        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End,
        ) {
            Text(
                text = "$totalPrice$",
                fontFamily = Inter,
                color = Color.White,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "$percentage%",
                fontFamily = Inter,
                color = percentageColor ?: Color.Transparent,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
            )
        }
    }
}


@Composable
private fun CoinsList(
    purchase: List<AggregatedPurchase>,
    details: List<FavoriteCoinDetails>?,
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

                ListItem(
                    purchase = item,
                    details = coinDetails
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

@Composable
private fun HelpWidget(
    navController: NavController,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = DarkBlue,
                shape = RoundedCornerShape(10.dp)
            )
            .drawBehind {
                drawRoundRect(
                    color = OutlineGray,
                    style = Stroke(
                        width = 1.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(
                            floatArrayOf(20f, 8f)
                        )
                    ),
                    cornerRadius = CornerRadius(
                        x = 10.dp.toPx(),
                        y = 10.dp.toPx()
                    )
                )
            }
            .clickable {
                navController.navigate(Screen.PurchaseHistory.route)
            }
            .padding(all = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 100.dp, end = 40.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Нет избранных покупок",
                fontFamily = Inter,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Записывайте покупки, чтобы они появлялись здесь.",
                fontFamily = Inter,
                fontWeight = FontWeight.Normal,
                fontSize = 11.sp,
                color = Color.Gray
            )
        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_wallet),
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(50.dp),
            )
        }
    }
}

@Composable
private fun ConclusionWidget(
    invested: String,
    profit: Double?,
    investedSum: Double,
) {

    val profitColor = when {
        profit == null -> Color.Gray
        profit >= 0 -> Green
        else -> Red
    }

    val profitText = profit?.let {
        if (profit >= 0.0) "+" + formatRate(value = profit) + " $"
        else formatRate(value = profit) + " $"
    } ?: "-- $"

    val isLoading = profit == null

    Box(
        modifier = Modifier
            .height(55.dp)
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
                .fillMaxSize()
                .padding(all = 10.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        color = Purple,
                        shape = RoundedCornerShape(10.dp)
                    )
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_wallet),
                    contentDescription = null,
                    tint = Lavender,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.5f)
            ) {
                Text(
                    text = "Вложено средств",
                    fontFamily = Inter,
                    fontWeight = FontWeight.Normal,
                    fontSize = 10.sp,
                    color = Color.Gray
                )
                Text(
                    text = "$invested $",
                    fontFamily = Inter,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.5f)
            ) {
                Text(
                    text = "Прибыль",
                    fontFamily = Inter,
                    fontWeight = FontWeight.Normal,
                    fontSize = 10.sp,
                    color = Color.Gray
                )
                if (investedSum != 0.0) {
                    if (isLoading) {
                        SkeletonBox(
                            Modifier
                                .width(70.dp)
                                .height(13.dp)
                        )
                    } else {
                        Text(
                            text = profitText,
                            fontFamily = Inter,
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp,
                            color = profitColor
                        )
                    }
                } else {
                    Text(
                        text = "0.0$",
                        fontFamily = Inter,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        color = profitColor
                    )
                }
            }
        }
    }
}

@Composable
private fun ListHat(
    isAscending: Boolean,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Мои активы",
            fontFamily = Inter,
            color = Color.White,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .clickable {
                    onClick()
                }
        ) {
            Text(
                text = "По стоимости",
                textAlign = TextAlign.End,
                fontFamily = Inter,
                color = Color.Gray,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
            )
            Spacer(modifier = Modifier.width(5.dp))
            Icon(
                painter = painterResource(
                    if (isAscending) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down
                ),
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(8.dp)
            )
        }
    }
}

fun sortPurchasesByPrice(
    purchases: List<AggregatedPurchase>,
    isAscending: Boolean
): List<AggregatedPurchase> {
    return if (isAscending) {
        purchases.sortedBy { it.totalValue }
    } else {
        purchases.sortedByDescending { it.totalValue }
    }
}

fun calculateInvested(
    purchases: List<PurchaseCoin>
): Double {
    val total = purchases.sumOf { it.amount * it.buyPrice }

    return total
}

fun calculateCurrentPrice(
    aggregatedPurchase: List<AggregatedPurchase>,
    details: List<FavoriteCoinDetails>?
): Double {
    var currentPrice = 0.0
    aggregatedPurchase.forEach { purchase ->
        details?.forEach { details ->
            if (purchase.coinId == details.id) {
                currentPrice += purchase.totalAmount * details.currentPrice
            }
        }
    }
    return currentPrice
}

fun calculateProfitPercentage(
    current: Double,
    invested: Double,
): Double {
    if (invested == 0.0) return 0.0

    return ((current - invested) / invested) * 100
}