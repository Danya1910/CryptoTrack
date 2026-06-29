package com.example.cryptotrack.presentation.widgets

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.cryptotrack.R
import com.example.cryptotrack.domain.model.MarketData
import com.example.cryptotrack.domain.util.MarketOrder
import com.example.cryptotrack.presentation.navigation.Screen
import com.example.cryptotrack.presentation.util.price.formatPrice
import com.example.cryptotrack.presentation.util.uiModels.StarParticle
import com.example.cryptotrack.presentation.viewmodel.CoinGeckoViewModel
import com.example.cryptotrack.presentation.viewmodel.CoinViewModel
import com.example.cryptotrack.ui.theme.DarkBlue
import com.example.cryptotrack.ui.theme.Green
import com.example.cryptotrack.ui.theme.Inter
import com.example.cryptotrack.ui.theme.OutlineGray
import com.example.cryptotrack.ui.theme.Red
import com.example.cryptotrack.ui.theme.Yellow
import kotlinx.coroutines.delay
import java.util.Locale
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.random.Random


@Composable
fun CoinMarketWidget(
    order: MarketOrder,
    coins: List<MarketData>?,
    viewModel: CoinGeckoViewModel,
    coinViewModel: CoinViewModel,
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
            coinViewModel = coinViewModel,
            navController = navController,
        )
    }
}

@Composable
fun CoinMarketWidgetSkeleton(
    order: MarketOrder,
    viewModel: CoinGeckoViewModel,
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
        CoinsMarketListSkeleton()
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
private fun CoinMarketItem(
    coin: MarketData?,
    navController: NavController,
    coinViewModel: CoinViewModel,
) {

    val isPositive =
        (coin?.priceChangePercentage24h ?: 0.0) >= 0

    val percentageColor =
        if (isPositive) Green
        else Red

    val percentageUsd = String.format(
        "%.1f",
        abs(coin?.priceChangePercentage24h ?: 0.0)
    )

    val currentPriceFormatted = formatPrice(value = coin?.currentPrice)

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

    val favoritesList by coinViewModel.favoriteCoins.collectAsState(initial = emptyList())

    val isFavorite = favoritesList.any { it.id == coin?.id }

    val scale = remember { Animatable(1f) }


    var showParticles by remember { mutableStateOf(false) }


    val particles = remember {
        List(8) {
            StarParticle(
                angle = Random.nextFloat() * 360f,
                distance = Random.nextFloat() * 50f + 120f
            )
        }
    }

    val particleProgress by animateFloatAsState(
        targetValue = if (showParticles) 1f else 0f,
        animationSpec = tween(400),
        label = "",
    )

    val filledAlpha by animateFloatAsState(
        targetValue = if (isFavorite) 1f else 0f,
        animationSpec = tween(300),
        label = ""
    )

    val outlineAlpha by animateFloatAsState(
        targetValue = if (isFavorite) 0f else 1f,
        animationSpec = tween(300),
        label = ""
    )

    LaunchedEffect(isFavorite) {
        if (isFavorite) {
            showParticles = true
            scale.animateTo(
                targetValue = 1.25f,
                animationSpec = tween(250)
            )
            scale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy
                )
            )
            delay(400)
            showParticles = false
        } else {
            scale.animateTo(
                1.2f,
                tween(150)
            )

            scale.animateTo(
                1.1f,
                tween(200)
            )
        }
    }

    Box(
        modifier = Modifier
            .height(36.dp)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable {
                    navController.navigate(Screen.CoinDetails.createRoute(id = coin?.id ?: ""))
                }
                .padding(horizontal = 10.dp)
                .fillMaxSize()
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
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(0.2f)
                    .graphicsLayer {
                        scaleX = scale.value
                        scaleY = scale.value
                    }
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        if (isFavorite) {
                            coinViewModel.deleteFavoriteCoin(id = coin?.id ?: "")
                        } else {
                            coinViewModel.insertFavoriteCoin(
                                id = coin?.id ?: "",
                                name = coin?.name ?: "",
                                symbol = coin?.symbol ?: "",
                                imageUrl = coin?.image ?: "",
                                timestamp = System.currentTimeMillis()
                            )
                        }
                    }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_star),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(14.dp)
                        .alpha(outlineAlpha)
                )
                Icon(
                    painter = painterResource(R.drawable.ic_fill_star),
                    contentDescription = null,
                    tint = Yellow,
                    modifier = Modifier
                        .size(14.dp)
                        .alpha(filledAlpha)
                )
            }

        }
        Box(
            contentAlignment = Alignment.CenterEnd,
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 10.dp)
        ) {
            if (showParticles) {
                particles.forEach { particle ->
                    val radians = Math.toRadians(
                        particle.angle.toDouble()
                    )
                    val x = cos(radians).toFloat() * particle.distance * particleProgress
                    val y = sin(radians).toFloat() * particle.distance * particleProgress
                    androidx.compose.material3.Icon(
                        painter = painterResource(
                            R.drawable.ic_fill_star
                        ),
                        contentDescription = null,
                        tint = Yellow,
                        modifier = Modifier
                            .size(8.dp)
                            .offset {
                                IntOffset(
                                    x.roundToInt(),
                                    y.roundToInt()
                                )
                            }
                            .alpha(1f - particleProgress)
                    )
                }
            }
        }
    }
}

@Composable
private fun CoinMarketItemSkeleton() {
    Row(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .height(36.dp)
            .fillMaxWidth()
            .background(DarkBlue),
        verticalAlignment = Alignment.CenterVertically
    ) {

        SkeletonBox(Modifier.weight(0.2f))

        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SkeletonBox(
                modifier = Modifier
                    .size(25.dp)
                    .clip(shape = CircleShape),
            )

            Spacer(Modifier.width(8.dp))

            Column {
                SkeletonBox(
                    Modifier
                        .width(80.dp)
                        .height(10.dp)
                )
                Spacer(Modifier.height(5.dp))
                SkeletonBox(
                    Modifier
                        .width(40.dp)
                        .height(10.dp)
                )
            }
        }
        Spacer(Modifier.width(8.dp))

        SkeletonBox(
            Modifier
                .weight(0.7f)
                .height(10.dp)
        )

        Spacer(Modifier.width(8.dp))

        Row(
            modifier = Modifier.weight(1.4f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SkeletonBox(
                Modifier
                    .width(40.dp)
                    .height(10.dp)
            )

            SkeletonBox(
                Modifier
                    .width(60.dp)
                    .height(10.dp)
            )

            SkeletonBox(Modifier.size(14.dp))
        }
    }
}


@Composable
private fun CoinsMarketList(
    coins: List<MarketData>?,
    navController: NavController,
    coinViewModel: CoinViewModel,
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
                    CoinMarketItem(
                        coin = coin,
                        coinViewModel = coinViewModel,
                        navController = navController,
                    )
                    if (index != coins.size - 1) {
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

@Composable
private fun CoinsMarketListSkeleton() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(DarkBlue, RoundedCornerShape(10.dp))
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(10) {
                CoinMarketItemSkeleton()

                if (it != 9) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(OutlineGray)
                    )
                }
            }
        }
    }
}