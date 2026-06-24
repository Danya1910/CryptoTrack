package com.example.cryptotrack.presentation.screens

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cryptotrack.presentation.viewmodel.CoinGeckoViewModel
import com.example.cryptotrack.ui.theme.DarkBlue
import com.example.cryptotrack.ui.theme.Green
import com.example.cryptotrack.ui.theme.Red
import kotlin.math.sin
import kotlin.math.tanh
import kotlin.random.Random


@Composable
fun SplashScreen(
    viewModel: CoinGeckoViewModel,
    onMoveToMain: () -> Unit,
) {

    val isReady by viewModel.isSplashReady.collectAsStateWithLifecycle()

    LaunchedEffect(isReady) {
            if(isReady)
                onMoveToMain()
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = DarkBlue
            )
    ) {
        AnimatedGraph()
    }
}

data class CandleData(
    val initialHigh: Float,
    val initialLow: Float,
    val initialOpen: Float,
    val initialClose: Float,
    val isGreen: Boolean,
    val phaseOffset: Float // Индивидуальная фаза для рассинхронизации покачивания
)

@Composable
fun CandleVolatileSplashChart(modifier: Modifier = Modifier) {
    // 1. Генерируем фиксированный набор базовых свечей один раз
    val candles = remember {
        val random = Random(42) // Seed для стабильной генерации при пересборке
        List(15) { index ->
            val isGreen = random.nextBoolean()
            val open = random.nextFloat() * 0.4f + 0.3f
            val close = if (isGreen) open + 0.15f else open - 0.15f
            CandleData(
                initialHigh = maxOf(open, close) + random.nextFloat() * 0.1f,
                initialLow = minOf(open, close) - random.nextFloat() * 0.1f,
                initialOpen = open,
                initialClose = close,
                isGreen = isGreen,
                phaseOffset = random.nextFloat() * 2f * Math.PI.toFloat()
            )
        }
    }

    // 2. Бесконечная анимация времени для покачивания и движения вправо
    val infiniteTransition = rememberInfiniteTransition(label = "candle_market")

    // Анимация для "живого" дыхания рынка (покачивание вверх-вниз)
    val timeFactor by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "time"
    )

    // Анимация для плавного смещения всего графика вправо (эффект течения)
    val flowOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "flow"
    )

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp)
            .padding(horizontal = 16.dp)
    ) {
        val width = size.width
        val height = size.height

        // Вычисляем ширину одной свечи и шаг между ними
        val maxVisibleCandles = 13
        val candleStep = width / maxVisibleCandles
        val candleWidth = candleStep * 0.4f // Свеча занимает половину шага, остальное - отступ

        // Цвета для бычьих и медвежьих свечей (классический неоновый крипто-стиль)

        // Рисуем свечи с учетом смещения flowOffset (течение вправо)
        for (i in -1..candles.size) {
            val candle = candles.getOrNull((i + candles.size) % candles.size) ?: continue

            // Рассчитываем текущую X позицию для свечи
            val currentX = (i + flowOffset) * candleStep

            // Рисуем только те свечи, которые попадают в границы экрана
            if (currentX < -candleStep || currentX > width + candleStep) continue

            // 3. Эффект покачивания: слегка модифицируем значения через sin()
            // У каждой свечи свой phaseOffset, чтобы они качались не синхронно
            val volatility = sin(timeFactor + candle.phaseOffset) * 0.04f

            val high = (candle.initialHigh + volatility).coerceIn(0f, 1f) * height
            val low = (candle.initialLow + volatility).coerceIn(0f, 1f) * height
            val open = (candle.initialOpen + volatility).coerceIn(0f, 1f) * height
            val close = (candle.initialClose + volatility).coerceIn(0f, 1f) * height

            val candleColor = if (candle.isGreen) Green else Red

            // 4. Отрисовка фитиля свечи (Тонкая вертикальная линия High-Low)
            drawLine(
                color = candleColor,
                start = Offset(currentX + candleWidth / 2, height - high),
                end = Offset(currentX + candleWidth / 2, height - low),
                strokeWidth = 2.dp.toPx()
            )

            // 5. Отрисовка тела свечи (Прямоугольник Open-Close)
            val bodyTop = height - maxOf(open, close)
            val bodyBottom = height - minOf(open, close)
            val bodyHeight = maxOf(bodyBottom - bodyTop, 2.dp.toPx()) // Чтобы тело не исчезало в ноль

            drawRect(
                color = candleColor,
                topLeft = Offset(currentX, bodyTop),
                size = Size(candleWidth, bodyHeight)
            )
        }
    }
}


@Composable
private fun AnimatedGraph() {

    val infiniteTransition = rememberInfiniteTransition()

    val animationProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "progress"
    )

    Canvas(
        modifier = Modifier
            .height(300.dp)
            .fillMaxWidth()
    ) {
        val centerX = size.width / 2f
        val centerY = size.height / 2f

        // Задаем максимальный размах движения (амплитуду) в пикселях
        val amplitude = 50.dp.toPx()

        // 3. Высчитываем динамическую координату Y для точки
        // Мы берем центр экрана и смещаем его вверх/вниз в зависимости от прогресса анимации
        val currentY = centerY + (animationProgress - 0.5f) * 2f * amplitude

        // 4. Рисуем колеблющуюся точку
        drawCircle(
            color = Color(0xFF00E676), // Стартовый зеленый цвет
            radius = 12.dp.toPx(),
            center = Offset(centerX, currentY)
        )
    }
}