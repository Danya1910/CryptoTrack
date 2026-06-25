package com.example.cryptotrack.presentation.screens

import android.graphics.BlurMaskFilter
import android.graphics.LinearGradient
import android.graphics.PathMeasure
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cryptotrack.presentation.viewmodel.CoinGeckoViewModel
import com.example.cryptotrack.ui.theme.BlackBackground
import com.example.cryptotrack.ui.theme.DarkBlue
import com.example.cryptotrack.ui.theme.Green
import com.example.cryptotrack.ui.theme.OutlineGray
import com.example.cryptotrack.ui.theme.Red


@Composable
fun SplashScreen(
    viewModel: CoinGeckoViewModel,
    onMoveToMain: () -> Unit,
) {

    val isReady by viewModel.isSplashReady.collectAsStateWithLifecycle()

    LaunchedEffect(isReady) {
        if (isReady)
            onMoveToMain()
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = BlackBackground
            )
    ) {
        AnimatedGraph()
    }
}

@Composable
private fun AnimatedGraph() {
    var startDrawing by remember { mutableStateOf(false) }

    val drawingProgress by animateFloatAsState(
        targetValue = if (startDrawing) 1f else 0f,
        animationSpec = tween(2000),
    )

    LaunchedEffect(Unit) {
        startDrawing = true
    }

    val blurPaint = remember {
        android.graphics.Paint().apply {
            isAntiAlias = true
            style = android.graphics.Paint.Style.STROKE
            strokeJoin = android.graphics.Paint.Join.MITER
            strokeCap = android.graphics.Paint.Cap.BUTT
        }
    }

    val nativeColors = remember {
        intColorsOf(listOf(Green.copy(alpha = 0.5f), Red.copy(alpha = 0.5f)))
    }

    Canvas(
        modifier = Modifier
            .height(300.dp)
            .fillMaxWidth()
    ) {
        val width = size.width
        val height = size.height

        blurPaint.strokeWidth = 12.dp.toPx()
        if (blurPaint.maskFilter == null) {
            blurPaint.maskFilter = BlurMaskFilter(8.dp.toPx(), BlurMaskFilter.Blur.NORMAL)
        }
        if (blurPaint.shader == null) {
            blurPaint.shader = LinearGradient(
                0f, height * 0.3f, 0f, height * 0.7f,
                nativeColors,
                null,
                android.graphics.Shader.TileMode.CLAMP
            )
        }

        val waveGradient = Brush.linearGradient(
            colors = listOf(Green, Red),
            start = Offset(0f, height * 0.3f),
            end = Offset(0f, height * 0.7f)
        )

        val dotSpacing = 20.dp.toPx()
        val dotRadius = 1.dp.toPx()

        val dotsProgress = (drawingProgress * 1.3f).coerceAtMost(1f)
        val currentMaxX = dotsProgress * width

        var x = width * 0.1f
        while (x < width) {
            if (x <= currentMaxX * 0.9f) {
                var y = 0f
                while (y < height) {
                    drawCircle(
                        color = OutlineGray,
                        radius = dotRadius,
                        center = Offset(x, y)
                    )
                    y += dotSpacing
                }
            }
            x += dotSpacing
        }

        val fullWavePath = Path().apply {
            moveTo(x = width * 0.2f, y = height * 0.9f)
            lineTo(x = width * 0.25f, y = height * 0.7f)
            lineTo(x = width * 0.35f, y = height * 0.6f)
            lineTo(x = width * 0.45f, y = height * 0.4f)
            lineTo(x = width * 0.50f, y = height * 0.45f)
            lineTo(x = width * 0.65f, y = height * 0.3f)
            lineTo(x = width * 0.69f, y = height * 0.4f)
            lineTo(x = width * 0.75f, y = height * 0.2f)
            lineTo(x = width * 0.8f, y = height * 0.05f)
        }

        val animatedPartialPath = Path()
        val pathMeasure = PathMeasure(fullWavePath.asAndroidPath(), false)
        val currentLengthToDraw = pathMeasure.length * drawingProgress

        pathMeasure.getSegment(
            0f,
            currentLengthToDraw,
            animatedPartialPath.asAndroidPath(),
            true
        )

        drawIntoCanvas { canvas ->
            canvas.nativeCanvas.drawPath(animatedPartialPath.asAndroidPath(), blurPaint)
        }

        drawPath(
            path = animatedPartialPath,
            brush = waveGradient,
            style = Stroke(
                width = 4.dp.toPx(),
                join = StrokeJoin.Miter,
                cap = StrokeCap.Butt
            )
        )
    }
}


private fun intColorsOf(colors: List<Color>): IntArray {
    return colors.map { it.toArgb() }.toIntArray()
}