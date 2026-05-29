package com.example.cryptotrack.domain.model


data class GlobalMarket(
    val activeCryptocurrencies: Int,
    val markets: Int,
    val marketCapChangePercentage24hUsd: Double,
    val volumeChangePercentage24Usd: Double,
    val totalMarketCap: TotalMarketCap,
    val totalVolume: TotalVolume,
    val marketCapPercentage: MarketCapPercentage,
)

data class MarketCapPercentage(
    val btc: Double,
    val eth: Double,
)

