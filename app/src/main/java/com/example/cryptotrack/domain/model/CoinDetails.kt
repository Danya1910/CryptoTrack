package com.example.cryptotrack.domain.model

data class CoinDetails(
    val id: String,
    val symbol: String,
    val name: String,
    val description: String,
    val links: Links,
    val image: Image,
    val marketData: MarketDetailData,
)

data class Image(
    val thumb: String,
    val small: String,
    val large: String,
)



data class Links(
    val homepage: List<String?>,
    val blockchainSite: List<String?>,
    val officialForumUrl: List<String?>,
    val subredditUrl: String?,
)

data class MarketDetailData(
    val currentPrice: Price,
    val marketCap: Price,
    val circulatingSupply: Double?,
    val totalSupply: Double?,
    val maxSupply: Double?,
    val ath: Price,
    val athChangePercentage: Price,
    val athDate: PriceDate,

    val atl: Price,
    val atlChangePercentage: Price,
    val atlDate: PriceDate,
    val high24h: Price,
    val low24h: Price,
)

data class Price(
    val usd: Double?,
    val rub: Double?,
)

data class PriceDate(
    val usd: String?,
    val rub: String?,
)