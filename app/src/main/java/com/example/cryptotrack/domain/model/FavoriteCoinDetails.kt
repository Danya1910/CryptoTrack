package com.example.cryptotrack.domain.model

data class FavoriteCoinDetails (
    val id: String,
    val symbol: String,
    val name: String,
    val image: String,
    val currentPrice: Double,
    val priceChangePercentage24h: Double
)