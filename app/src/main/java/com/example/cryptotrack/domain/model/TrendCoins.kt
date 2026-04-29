package com.example.cryptotrack.domain.model

data class TrendCoins(
    val coins: List<TrendCoin>
)


data class TrendCoin(
    val id: String,
    val coinId: Int,
    val name: String,
    val symbol: String,
    val marketCapRank: Int,
    val thumb: String,
    val data: TrendData,
)

data class TrendData(
    val price: Double,
    val priceChangePercentage24h: PriceChange,
)

data class PriceChange(
    val usd: Double,
    val rub: Double?,
)
