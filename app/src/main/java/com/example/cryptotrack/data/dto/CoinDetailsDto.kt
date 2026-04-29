package com.example.cryptotrack.data.dto


data class CoinDetailsDto(
    val id: String,
    val symbol: String,
    val name: String,
    val description: DescriptionDto,
    val links: LinksDto,
    val image: ImageDto,
    val market_data: MarketDetailDataDto,
)

data class ImageDto(
    val thumb: String,
    val small: String,
    val large: String,
)

data class DescriptionDto(
    val en: String?,
)

data class LinksDto(
    val homepage: List<String?>,
    val blockchain_site: List<String?>,
    val official_forum_url: List<String?>,
    val subreddit_url: String?,
)

data class MarketDetailDataDto(
    val current_price: PriceDto,
    val market_cap: PriceDto,
    val circulating_supply: Double?,
    val total_supply: Double?,
    val max_supply: Double?,
    val ath: PriceDto,
    val ath_change_percentage: PriceDto,
    val ath_date: DateDto,
    val high_24h: PriceDto,
    val low_24h: PriceDto,

    val atl: PriceDto,
    val atl_change_percentage: PriceDto,
    val atl_date: DateDto,
)

data class PriceDto(
    val usd: Double?,
    val rub: Double?,
)

data class DateDto(
    val usd: String?,
    val rub: String?,
)