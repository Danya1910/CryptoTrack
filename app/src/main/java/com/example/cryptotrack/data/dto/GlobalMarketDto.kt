package com.example.cryptotrack.data.dto

data class GlobalMarketDto(
    val active_cryptocurrencies: Int,
    val markets: Int,
    val market_cap_change_percentage_24h_usd: Double,
    val total_market_cap: TotalMarketCapDto,
    val total_volume: TotalVolumeDto,
)
