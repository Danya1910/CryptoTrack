package com.example.cryptotrack.data.dto

data class TrendCoinsDto(
    val coins: List<TrendItemDto>
)

data class TrendItemDto(
    val item: TrendCoinDto
)


data class TrendCoinDto(
    val id: String,
    val coin_id: Int,
    val name: String,
    val symbol: String,
    val market_cap_rank: Int,
    val thumb: String,
    val data: TrendDataDto,

)

data class TrendDataDto(
    val price: Double,
    val price_change_percentage_24h: PriceChangeDto,
)

data class PriceChangeDto(
    val usd: Double,
    val rub: Double?,
)