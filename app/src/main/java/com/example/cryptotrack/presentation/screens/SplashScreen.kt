package com.example.cryptotrack.presentation.screens

import android.graphics.BlurMaskFilter
import android.graphics.LinearGradient
import android.graphics.PathMeasure
import android.graphics.Shader
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cryptotrack.presentation.viewmodel.CoinGeckoViewModel
import com.example.cryptotrack.ui.theme.BlackBackground
import com.example.cryptotrack.ui.theme.DarkBlue
import com.example.cryptotrack.ui.theme.DarkGray
import com.example.cryptotrack.ui.theme.Green
import com.example.cryptotrack.ui.theme.OutlineGray
import com.example.cryptotrack.ui.theme.Red
import com.example.cryptotrack.ui.theme.Yellow


@Composable
fun SplashScreen(
    viewModel: CoinGeckoViewModel,
    onMoveToMain: () -> Unit,
) {

    val isReady by viewModel.isSplashReady.collectAsStateWithLifecycle()

    var startDrawing by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        startDrawing = true
    }

    LaunchedEffect(isReady) {
        if (isReady)
            onMoveToMain()
    }


    val drawingProgress by animateFloatAsState(
        targetValue = if (startDrawing) 1f else 0f,
        animationSpec = tween(2000),
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(
               brush = Brush.linearGradient(
                   colors = listOf(BlackBackground, DarkGray),
                   start = Offset(0f, Float.POSITIVE_INFINITY),
                   end = Offset(Float.POSITIVE_INFINITY, 0f)
               )
            )
    ) {
        AnimatedGraph(
            drawingProgress = drawingProgress
        )

        Spacer(modifier = Modifier.height(10.dp))

        AnimatedTitle(
            drawingProgress = drawingProgress
        )
    }
}

@Composable
private fun AnimatedTitle(
    drawingProgress: Float,
) {
    Text(
        text = "CryptoTrack",
        fontSize = 36.sp,
        fontWeight = FontWeight.SemiBold,
        fontFamily = FontFamily.Monospace,
        modifier = Modifier
            .graphicsLayer { alpha = 0.99f }
            .drawWithContent {
                val textWidth = size.width

                val maskBrush = Brush.linearGradient(
                    (drawingProgress - 0.05f).coerceIn(0f, 1f) to Color.White,
                    drawingProgress to Color.White,
                    (drawingProgress + 0.01f).coerceIn(0f, 1f) to Color.Transparent,

                    start = Offset(0f, 0f),
                    end = Offset(textWidth, 0f)
                )

                drawContent()

                drawRect(
                    brush = maskBrush,
                    blendMode = BlendMode.SrcIn,
                )

            }
    )
}


@Composable
private fun AnimatedGraph(
    drawingProgress: Float,
) {
    val blurPaint = remember {
        android.graphics.Paint().apply {
            isAntiAlias = true
            style = android.graphics.Paint.Style.STROKE
            strokeJoin = android.graphics.Paint.Join.MITER
            strokeCap = android.graphics.Paint.Cap.BUTT
        }
    }

    val nativeColors = remember {
        intColorsOf(
            listOf(
                Green.copy(alpha = 0.5f),
                Yellow.copy(alpha = 0.5f),
                Red.copy(alpha = 0.5f)
            )
        )
    }
    val fullWavePath = remember { Path() }
    val animatedPartialPath = remember { Path() }
    val pathMeasureRef = remember { arrayOfNulls<PathMeasure>(1) }
    var isPathInitialized by remember { mutableStateOf(false) }

    Canvas(
        modifier = Modifier
            .height(300.dp)
            .fillMaxWidth()
    ) {
        val width = size.width
        val height = size.height

        // Инициализируем геометрию ОДИН РАЗ за всю жизнь экрана
        if (!isPathInitialized) {
            fullWavePath.apply {
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
            pathMeasureRef[0] = PathMeasure(fullWavePath.asAndroidPath(), false)
            isPathInitialized = true
        }

        blurPaint.strokeWidth = 12.dp.toPx()
        if (blurPaint.maskFilter == null) {
            blurPaint.maskFilter = BlurMaskFilter(8.dp.toPx(), BlurMaskFilter.Blur.NORMAL)
        }
        if (blurPaint.shader == null) {
            blurPaint.shader = LinearGradient(
                0f, height * 0.3f, 0f, height * 0.7f,
                nativeColors, null, Shader.TileMode.CLAMP
            )
        }

        val waveGradient = Brush.linearGradient(
            colors = listOf(Green, Red),
            start = Offset(0f, height * 0.3f),
            end = Offset(0f, height * 0.7f)
        )

        // ОТРИСОВКА ТОЧЕК (Остается без изменений)
        val dotSpacing = 20.dp.toPx()
        val dotRadius = 1.dp.toPx()
        val dotsProgress = (drawingProgress * 1.3f).coerceAtMost(1f)
        val currentMaxX = dotsProgress * width

        var x = width * 0.1f
        while (x < width) {
            if (x <= currentMaxX * 0.9f) {
                var y = 0f
                while (y < height) {
                    drawCircle(color = OutlineGray, radius = dotRadius, center = Offset(x, y))
                    y += dotSpacing
                }
            }
            x += dotSpacing
        }

        // РАБОТА С ОТРЕЗАННЫМ ПУТЕМ (Используем закэшированный pathMeasure)
        val pathMeasure = pathMeasureRef[0]
        if (pathMeasure != null) {
            animatedPartialPath.rewind() // Очищаем старый пройденный путь перед новой записью, не выделяя новую память
            val currentLengthToDraw = pathMeasure.length * drawingProgress
            pathMeasure.getSegment(0f, currentLengthToDraw, animatedPartialPath.asAndroidPath(), true)
        }

        // Отрисовка свечения
        drawIntoCanvas { canvas ->
            canvas.nativeCanvas.drawPath(animatedPartialPath.asAndroidPath(), blurPaint)
        }

        // Отрисовка основной линии
        drawPath(
            path = animatedPartialPath,
            brush = waveGradient,
            style = Stroke(width = 4.dp.toPx(), join = StrokeJoin.Miter, cap = StrokeCap.Butt)
        )
    }
}


private fun intColorsOf(colors: List<Color>): IntArray {
    return colors.map { it.toArgb() }.toIntArray()
}