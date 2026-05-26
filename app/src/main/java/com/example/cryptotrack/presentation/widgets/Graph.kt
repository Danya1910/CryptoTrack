package com.example.cryptotrack.presentation.widgets

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cryptotrack.domain.model.CoinChart
import com.example.cryptotrack.domain.model.CoinsChartList
import com.example.cryptotrack.domain.model.GraphData
import com.example.cryptotrack.ui.theme.BlackBackground
import com.example.cryptotrack.ui.theme.Green
import com.example.cryptotrack.ui.theme.Inter
import com.example.cryptotrack.ui.theme.Red
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@Composable
fun Graph(
    chart: CoinsChartList?
) {

    val chartData = chart?.list ?: return

    val graphColor =
        if (chartData[0].price < chartData[chartData.size - 1].price) {
            Green
        } else Red


    val formatter = DateTimeFormatter.ofPattern("HH:mm")

    val textMeasurer = rememberTextMeasurer()

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .background(BlackBackground)
    ) {

        val minPrice = chartData.minOf { it.price }
        val maxPrice = chartData.maxOf { it.price }

        val priceLabelWidth = calculatePriceLabelWidth(
            minPrice = minPrice,
            maxPrice = maxPrice,
            textMeasurer = textMeasurer
        )

        val graphData = calculateGraphData(
            chartData = chartData,
            canvasWidth = size.width,
            canvasHeight = size.height,
            rightPadding = priceLabelWidth + 20f
        )

        val graphPath = createGraphPath(
            points = graphData.points
        )

        val areaPath = createAreaPath(
            points = graphData.points,
            bottomY = graphData.bottomY
        )

        drawGraphArea(areaPath, graphColor)

        drawDots(areaPath, graphColor)

        drawGraphLine(graphPath, graphColor)

        drawTimeLabels(
            chartData = chartData,
            graphData = graphData,
            formatter = formatter,
            textMeasurer = textMeasurer
        )

        drawPriceLabels(
            graphData = graphData,
            textMeasurer = textMeasurer
        )

    }
}

private fun calculateGraphData(
    chartData: List<CoinChart>,
    canvasWidth: Float,
    canvasHeight: Float,
    rightPadding: Float
): GraphData {


    val graphWidth = canvasWidth - rightPadding

    val topPadding = canvasHeight * 0.1f
    val bottomPadding = canvasHeight * 0.05f

    val graphHeight = canvasHeight - topPadding - bottomPadding

    val minPrice = chartData.minOf { it.price }
    val maxPrice = chartData.maxOf { it.price }

    val minTime = chartData.minOf { it.time }
    val maxTime = chartData.maxOf { it.time }

    val priceRange = (maxPrice - minPrice)
        .takeIf { it != 0.0 }
        ?: 1.0

    val timeRange = (maxTime - minTime)
        .takeIf { it != 0L }
        ?: 1L

    val points = chartData.map { point ->

        val x = (
                (point.time - minTime) / timeRange.toFloat()
                ) * graphWidth

        val normalizedY = (
                (point.price - minPrice) / priceRange
                ).toFloat()

        val y = topPadding + graphHeight * (1f - normalizedY)

        Offset(x, y)
    }

    return GraphData(
        points = points,
        minPrice = minPrice,
        maxPrice = maxPrice,
        minTime = minTime,
        maxTime = maxTime,
        topPadding = topPadding,
        bottomPadding = bottomPadding,
        graphHeight = graphHeight,
        bottomY = topPadding + graphHeight,
        graphWidth = graphWidth
    )
}

@SuppressLint("DefaultLocale")
private fun formatPrice(price: Double): String {

    return when {

        price >= 1_000_000 -> {
            String.format("%.1fm", price / 1_000_000)
        }

        price >= 1_000 -> {
            String.format("%.1fk", price / 1_000)
        }

        price >= 1 -> {
            String.format("%.2f", price)
        }

        price >= 0.01 -> {
            String.format("%.4f", price)
        }

        else -> {
            String.format("%.8f", price)
        }
    }
}

@SuppressLint("DefaultLocale")
private fun calculatePriceLabelWidth(
    minPrice: Double,
    maxPrice: Double,
    textMeasurer: TextMeasurer
): Float {

    val steps = 5

    val priceStep = (maxPrice - minPrice) / steps

    var maxWidth = 0f

    for (i in 1..steps) {

        val currentPrice = minPrice + i * priceStep

        val priceText = formatPrice(currentPrice)

        val textLayout = textMeasurer.measure(
            text = priceText,
            style = labelStyle()
        )

        maxWidth = maxOf(
            maxWidth,
            textLayout.size.width.toFloat()
        )
    }

    return maxWidth
}

private fun createGraphPath(
    points: List<Offset>
): Path {
    return Path().apply {
        points.forEachIndexed { index, point ->
            if (index == 0) {
                moveTo(point.x, point.y)
            } else {
                lineTo(point.x, point.y)
            }
        }
    }
}

private fun createAreaPath(
    points: List<Offset>,
    bottomY: Float
): Path {
    return Path().apply {
        points.forEachIndexed { index, point ->
            if (index == 0) {
                moveTo(point.x, point.y)
            } else {
                lineTo(point.x, point.y)
            }
        }
        lineTo(points.last().x, bottomY)
        lineTo(points.first().x, bottomY)
        close()
    }
}

private fun DrawScope.drawGraphLine(
    graphPath: Path,
    graphColor: Color,
) {
    drawPath(
        path = graphPath,
        color = graphColor,
        style = Stroke(width = 5f)
    )
}

private fun DrawScope.drawGraphArea(
    areaPath: Path,
    graphColor: Color,
) {
    drawPath(
        path = areaPath,
        brush = Brush.verticalGradient(
            listOf(
                graphColor.copy(alpha = 0.7f),
                graphColor.copy(alpha = 0.1f)
            )
        )
    )
}

private fun DrawScope.drawDots(
    arePath: Path,
    graphColor: Color,
) {
    clipPath(arePath) {
        val dotSpacing = 20f
        val dotRadius = 2f

        var y = 0f

        while (y < size.height) {
            var x = 0f
            while (x < size.width) {
                drawCircle(
                    color = graphColor.copy(alpha = 0.5f),
                    radius = dotRadius,
                    center = Offset(x, y)
                )
                x += dotSpacing
            }
            y += dotSpacing
        }
    }
}

private fun DrawScope.drawTimeLabels(
    chartData: List<CoinChart>,
    graphData: GraphData,
    formatter: DateTimeFormatter,
    textMeasurer: TextMeasurer
) {
    val timeSteps = 5

    for (i in 0..timeSteps) {
        val index = i * (chartData.size - 1) / timeSteps
        val point = chartData[index]
        val text = Instant
            .ofEpochMilli(point.time)
            .atZone(ZoneId.systemDefault())
            .format(formatter)
        val textLayout = textMeasurer.measure(
            text = text,
            style = labelStyle()
        )
        val rawX = graphData.points[index].x
        val x = rawX
            .coerceAtLeast(textLayout.size.width / 2f + 10f)
            .coerceAtMost(size.width - textLayout.size.width / 2f - 10f)
        drawText(
            textLayoutResult = textLayout,
            topLeft = Offset(
                x = x - textLayout.size.width / 2,
                y = graphData.bottomY + 20f
            )
        )
    }
}

@SuppressLint("DefaultLocale")
private fun DrawScope.drawPriceLabels(
    graphData: GraphData,
    textMeasurer: TextMeasurer
) {

    val steps = 5

    val priceStep = (
            graphData.maxPrice - graphData.minPrice
            ) / steps

    for (i in 1..steps) {

        val currentPrice =
            graphData.minPrice + i * priceStep

        val y = graphData.topPadding +
                graphData.graphHeight * (1f - i / steps.toFloat())

        val priceText = formatPrice(currentPrice)

        val textLayout = textMeasurer.measure(
            text = priceText,
            style = labelStyle()
        )

        val textX =
            size.width - textLayout.size.width - 10f

        drawText(
            textLayoutResult = textLayout,
            topLeft = Offset(
                x = textX,
                y = y - textLayout.size.height / 2
            )
        )

        drawLine(
            color = Color.White,
            start = Offset(0f, y),
            end = Offset(textX - 10f, y)
        )
    }
}

fun labelStyle(): TextStyle {

    return TextStyle(
        color = Color.White,
        fontSize = 12.sp,
        fontFamily = Inter,
        fontWeight = FontWeight.Medium,

        )
}