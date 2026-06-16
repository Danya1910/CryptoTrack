package com.example.cryptotrack.presentation.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
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
import com.example.cryptotrack.presentation.util.price.formatRate
import com.example.cryptotrack.presentation.util.price.formatTimeAndDate
import com.example.cryptotrack.presentation.util.price.getCoinPlural
import com.example.cryptotrack.presentation.viewmodel.CoinGeckoViewModel
import com.example.cryptotrack.presentation.viewmodel.CoinViewModel
import com.example.cryptotrack.presentation.widgets.PurchaseHistoryTopAppBar
import com.example.cryptotrack.ui.theme.BlackBackground
import com.example.cryptotrack.ui.theme.DarkBlue
import com.example.cryptotrack.ui.theme.Green
import com.example.cryptotrack.ui.theme.Inter
import com.example.cryptotrack.ui.theme.Lavender
import com.example.cryptotrack.ui.theme.OutlineGray
import com.example.cryptotrack.ui.theme.Purple
import com.example.cryptotrack.ui.theme.Red
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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

    val uiList = remember { mutableStateListOf<PurchaseCoin>() }

    LaunchedEffect(purchase) {
        if (uiList.isEmpty() && purchase.isNotEmpty())
            uiList.addAll(purchase)
    }

    LaunchedEffect(Unit) {
        if (purchase.isNotEmpty()) {
            val ids = purchase.joinToString(",") { it.coinId }
            viewModel.getFavoriteCoinsDetails(ids = ids)
        }
    }

    val purchaseDetails by viewModel.favoriteCoinsDetailsState.collectAsState()

    val details by remember(purchaseDetails.details) {
        mutableStateOf(purchaseDetails.details)
    }

    val isApiDataAvailable = !details.isNullOrEmpty()

    val purchasesCount = purchase.size.toString()
    val investedSum = calculateInvested(purchases = purchase)

    val currentSum = if (isApiDataAvailable) {
        calculateCurrentPrice(aggregatedPurchase = aggregatedPurchase, details = details)
    } else null


    val currentFormatted = currentSum?.let { formatRate(value = it) }
    val investedFormatted = formatRate(value = investedSum)
    val profitPercentage = if (isApiDataAvailable && currentSum != null)
        calculateProfitPercentage(
            current = currentSum,
            invested = investedSum,
        )
    else null

    var showCancel by remember { mutableStateOf(false) }
    val cancelDeleting = remember { mutableStateOf(false) }
    val clicked = remember { mutableStateOf(false) }


    LaunchedEffect(showCancel) {
        if (showCancel) {
            delay(3000)
            showCancel = false
            clicked.value = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
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
                currentPrice = currentFormatted,
                profitPercentage = profitPercentage
            )
            Spacer(modifier = Modifier.height(10.dp))
            if (purchase.isEmpty()) {
                HelpWidget(
                    navController = navController,
                )
            } else {
                PurchasesList(
                    purchase = uiList,
                    details = details,
                    onDeleteClick = { item ->
                        uiList.remove(item)
                        coinViewModel.deletePurchasedCoin(
                            id = item.id,
                            coinId = item.coinId,
                            name = item.name,
                            amount = item.amount,
                            buyPrice = item.buyPrice,
                            buyDate = item.buyDate,
                            imageUrl = item.imageUrl,
                        )
                    },
                    navController = navController,
                    showCancelTrigger = {
                        showCancel = it
                    },
                    cancelDeleting = cancelDeleting,
                    clicked = clicked,
                )
            }
        }
        if (showCancel) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .align(Alignment.BottomCenter)
                    .clickable {
                        cancelDeleting.value = true
                        showCancel = false
                        clicked.value = true
                    }
            ) {
                Text(
                    text = "Всего покупок",
                    fontFamily = Inter,
                    color = Color.Gray,
                    fontWeight = FontWeight.Normal,
                    fontSize = 10.sp,
                )
            }
        }
    }
}

@Composable
private fun HelpWidget(
    navController: NavController,
) {

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .clickable{
                navController.navigate(Screen.AddPurchase.route)
            }
            .padding(horizontal = 60.dp)
    ) {
            Icon(
                painter = painterResource(R.drawable.ic_clock),
                contentDescription = null,
                tint = Lavender,
                modifier = Modifier.size(50.dp),
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "История покупок пуста",
                fontFamily = Inter,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                textAlign = TextAlign.Center,
                text = "После первой покупки здесь будут отображаться все операции.",
                fontFamily = Inter,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                textAlign = TextAlign.Center,
                text = "Добавить покупку",
                fontFamily = Inter,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                textDecoration = TextDecoration.Underline,
                color = Lavender
            )
        }
}

@Composable
private fun PurchaseInfoHat(
    purchaseCount: String,
    invested: String,
    currentPrice: String?,
    profitPercentage: Double?,
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
                    text = "$displayCurrentPrice $",
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
    details: FavoriteCoinDetails?,
    onClick: () -> Unit,
    navController: NavController,
    modifier: Modifier,
    showSwipeHint: Boolean,
    showCancelTrigger: (Boolean) -> Unit,
    cancelDeleting: MutableState<Boolean>,
    clicked: MutableState<Boolean>,
) {

    val scope = rememberCoroutineScope()

    val buyPrice = formatRate(purchase.buyPrice)

    val firstInvestedDate = formatTimeAndDate(millis = purchase.buyDate)

    val hintOffset = remember { Animatable(0f) }

    var deleted by remember {
        mutableStateOf(false)
    }

    val height by animateDpAsState(
        if (deleted) 0.dp else 60.dp
    )

    val insteadOfSymbol = if (details?.symbol.isNullOrEmpty()) {
        getCoinPlural(purchase.amount)
    } else {
        details.symbol.uppercase()
    }

    LaunchedEffect(showSwipeHint) {
        if (showSwipeHint) {
            delay(1000)
            hintOffset.animateTo(
                targetValue = -220f,
                animationSpec = tween(250)
            )
            hintOffset.animateTo(
                targetValue = -220f,
                animationSpec = tween(250)
            )
            hintOffset.animateTo(
                targetValue = 0f,
                animationSpec = tween(250)
            )
        }
    }

    val offsetX = remember { Animatable(0f) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .height(height)
    ) {
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(
                    color = Red
                )
                .clickable {
                    onClick()
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
                    IntOffset((offsetX.value + hintOffset.value).roundToInt(), 0)
                }
                .background(color = DarkBlue)
                .clickable {
                    navController.navigate(Screen.CoinDetails.createRoute(id = purchase.coinId))
                }
                .padding(horizontal = 10.dp, vertical = 5.dp)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { _, dragAmount ->
                            scope.launch {
                                offsetX.snapTo(
                                    (offsetX.value + dragAmount)
                                        .coerceIn(-650f, 0f)
                                )
                            }
                        },
                        onDragEnd = {
                            when {
                                offsetX.value < -300f -> {
                                    scope.launch {

                                        deleted = true
                                        showCancelTrigger(true)

                                        offsetX.animateTo(
                                            targetValue = -1000f,
                                            animationSpec = tween(250)
                                        )

                                        delay(3000)


                                        if (clicked.value) {

                                            if (cancelDeleting.value) {

                                                offsetX.snapTo(-1000f)

                                                offsetX.animateTo(
                                                    targetValue = 0f,
                                                    animationSpec = tween(300)
                                                )

                                                cancelDeleting.value = false
                                                deleted = false
                                                showCancelTrigger(false)
                                            }
                                            clicked.value = false
                                        } else {
                                            onClick()
                                            showCancelTrigger(false)
                                        }
                                    }
                                }

                                offsetX.value < -180 -> {
                                    scope.launch {
                                        offsetX.animateTo(
                                            -220f,
                                            tween(200)
                                        )
                                    }
                                }

                                else -> {
                                    scope.launch {
                                        offsetX.animateTo(
                                            0f,
                                            tween(200)
                                        )
                                    }
                                }
                            }
                        }
                    )
                },
        ) {
            AsyncImage(
                model = purchase.imageUrl,
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
                    text = "${purchase.amount} $insteadOfSymbol",
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
    onDeleteClick: (PurchaseCoin) -> Unit,
    navController: NavController,
    showCancelTrigger: (Boolean) -> Unit,
    cancelDeleting: MutableState<Boolean>,
    clicked: MutableState<Boolean>,
) {


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(
                color = DarkBlue,
            )
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(DarkBlue)
        ) {
            itemsIndexed(
                items = purchase,
                key = { _, item -> item.id }
            ) { index, item ->
                val coinDetails = details?.find { it.id == item.coinId }

                HistoryItem(
                    purchase = item,
                    details = coinDetails,
                    onClick = { onDeleteClick(item) },
                    navController = navController,
                    modifier = Modifier.animateItem(),
                    showSwipeHint = index == 0,
                    showCancelTrigger = showCancelTrigger,
                    cancelDeleting = cancelDeleting,
                    clicked = clicked,
                )

                if (index != purchase.lastIndex) {
                    Box(
                        modifier = Modifier
                            .height(1.dp)
                            .fillMaxWidth()
                            .background(OutlineGray)
                    )
                }
            }
        }
    }
}