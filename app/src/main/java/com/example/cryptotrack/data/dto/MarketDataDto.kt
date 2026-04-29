package com.example.cryptotrack.data.dto

data class MarketDataDto(
    val id: String,
    val symbol: String,
    val name: String,
    val image: String,
    val current_price: Double?,
    val market_cap: Double?,
    val market_cap_rank: Int?,
    val last_updated: String?,
    val price_change_percentage_24h: Double?,
)