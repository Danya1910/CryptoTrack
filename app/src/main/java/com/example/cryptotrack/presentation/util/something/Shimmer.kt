package com.example.cryptotrack.presentation.util.something

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import com.example.cryptotrack.ui.theme.DarkGray
import com.example.cryptotrack.ui.theme.HelpDarkBlue


@Composable
fun Modifier.shimmer() : Modifier {

    val trainsition = rememberInfiniteTransition(label = "")

    val transitionAnim = trainsition.animateFloat(
        initialValue = -1000f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1200,
                easing = LinearEasing,
            )
        ),
        label = ""
    )

    val brush = Brush.linearGradient(
        colors = listOf(
            DarkGray,
            HelpDarkBlue,
            DarkGray
        ),
        start = Offset(transitionAnim.value, 0f),
        end = Offset(transitionAnim.value + 300f, 300f)
    )


    return this.background(brush = brush)

}