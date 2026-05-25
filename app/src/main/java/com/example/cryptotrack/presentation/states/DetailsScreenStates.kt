package com.example.cryptotrack.presentation.states

import com.example.cryptotrack.domain.model.CoinDetails
import com.example.cryptotrack.domain.model.CoinsChartList

data class DetailsScreenStates(
    val details: CoinDetails? = null,
    val chart: CoinsChartList? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)