package com.example.cryptotrack.presentation.states

import com.example.cryptotrack.domain.model.CoinDetails
import com.example.cryptotrack.domain.model.CoinsChartList

data class DetailsState(
    val details: CoinDetails? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)

data class ChartState(
    val chart: CoinsChartList? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)