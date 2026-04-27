package com.example.cryptotrack.domain.model

data class MarketData(
    val id: String,
    val symbol: String,
    val name: String,
    val image: String,
    val currentPrice: Double,
    val marketCap: Double,
    val marketCapRank: Int,
    val lastUpdated: String,
    val priceChange24h: Double,
)
