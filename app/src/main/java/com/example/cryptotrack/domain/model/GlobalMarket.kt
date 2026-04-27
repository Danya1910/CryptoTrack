package com.example.cryptotrack.domain.model


data class GlobalMarket(
    val activeCryptocurrencies: Int,
    val markets: Int,
    val marketCapChangePercentage24hUsd: Double,
    val totalMarketCap: TotalMarketCap,
    val totalVolume: TotalVolume,
)

