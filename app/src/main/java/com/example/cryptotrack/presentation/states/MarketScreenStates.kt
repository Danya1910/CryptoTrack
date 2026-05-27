package com.example.cryptotrack.presentation.states

import com.example.cryptotrack.domain.model.GlobalMarket
import com.example.cryptotrack.domain.model.MarketData
import com.example.cryptotrack.domain.model.TrendCoins

data class GlobalMarketState (
    val globalMarket:  GlobalMarket? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)

data class MarketDataState (
    val market: List<MarketData>? = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

data class TrendState(
    val trendCoins: TrendCoins? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)