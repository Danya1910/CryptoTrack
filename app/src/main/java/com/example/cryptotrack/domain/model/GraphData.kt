package com.example.cryptotrack.domain.model

import androidx.compose.ui.geometry.Offset

data class GraphData(
    val points: List<Offset>,

    val minPrice: Double,
    val maxPrice: Double,

    val minTime: Long,
    val maxTime: Long,

    val topPadding: Float,
    val bottomPadding: Float,

    val graphHeight: Float,

    val bottomY: Float,
    val graphWidth: Float,
)