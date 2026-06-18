package com.example.cryptotrack.presentation.widgets

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.cryptotrack.R
import com.example.cryptotrack.domain.model.CoinDetails
import com.example.cryptotrack.presentation.navigation.Screen
import com.example.cryptotrack.presentation.util.uiModels.StarParticle
import com.example.cryptotrack.presentation.viewmodel.CoinViewModel
import com.example.cryptotrack.ui.theme.BlackBackground
import com.example.cryptotrack.ui.theme.Inter
import com.example.cryptotrack.ui.theme.Orange
import com.example.cryptotrack.ui.theme.Yellow
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.random.Random


@Composable
fun DetailsTopAppBar(
    navController: NavController,
    coinViewModel: CoinViewModel,
    coinId: String,
    details: CoinDetails?,
) {

    val favoriteCoins by coinViewModel.favoriteCoins.collectAsState(initial = emptyList())

    val isFavorite = favoriteCoins.any { it.id == coinId }

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
            .fillMaxWidth()
            .height(60.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(60.dp)
                .fillMaxWidth()
                .background(
                    color = BlackBackground,
                )
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
            Text(
                text = "Coin Details",
                color = Color.White,
                fontFamily = Inter,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.weight(1f)
            )
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(30.dp)
                    .graphicsLayer {
                        scaleX = scale.value
                        scaleY = scale.value
                    }
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        if (isFavorite) {
                            coinViewModel.deleteFavoriteCoin(coinId)
                        } else {
                            details?.let {
                                coinViewModel.insertFavoriteCoin(
                                    id = it.id,
                                    name = it.name,
                                    symbol = it.symbol,
                                    imageUrl = it.image.thumb,
                                    timestamp = System.currentTimeMillis()
                                )
                            }
                        }
                    }
            ) {

                Icon(
                    painter = painterResource(R.drawable.ic_star),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(22.dp)
                        .alpha(outlineAlpha)
                )

                Icon(
                    painter = painterResource(R.drawable.ic_fill_star),
                    contentDescription = null,
                    tint = Yellow,
                    modifier = Modifier
                        .size(22.dp)
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