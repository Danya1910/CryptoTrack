package com.example.cryptotrack.domain.model


data class CoinsChartList(
    val list: List<CoinChart>
)

data class CoinChart(
    val time: Long,
    val price: Double,
)
