package com.example.cryptotrack.presentation.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cryptotrack.domain.model.CoinsChartList
import com.example.cryptotrack.ui.theme.Green
import com.example.cryptotrack.ui.theme.Red
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.cryptotrack.domain.model.CoinChart
import com.example.cryptotrack.domain.model.GraphData
import com.example.cryptotrack.ui.theme.DarkBlue


@Composable
fun HistoryGraph(
    chart: CoinsChartList?,
) {

    val chartData = chart?.list ?: return

    val graphColor =
        if (chartData[0].price <= chartData[chartData.size - 1].price) {
            Green
        } else
            Red

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(DarkBlue)
    ) {

        val graphData = calculateGraphData(
            chartData = chartData,
            canvasWidth = size.width,
            canvasHeight = size.height,
        )

        val graphPath = createGraphPath(
            points = graphData.points
        )

        val areaPath = createAreaPath(
            points = graphData.points,
            bottomY = graphData.bottomY
        )

        drawGraphArea(areaPath, graphColor)

        drawGraphLine(graphPath, graphColor)

    }

}

private fun calculateGraphData(
    chartData: List<CoinChart>,
    canvasWidth: Float,
    canvasHeight: Float,

): GraphData {


    val graphWidth = canvasWidth

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

