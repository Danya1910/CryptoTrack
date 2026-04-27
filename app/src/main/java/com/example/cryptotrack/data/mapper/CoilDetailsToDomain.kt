package com.example.cryptotrack.data.mapper

import com.example.cryptotrack.data.dto.CoinDetailsDto
import com.example.cryptotrack.data.dto.DateDto
import com.example.cryptotrack.data.dto.ImageDto
import com.example.cryptotrack.data.dto.LinksDto
import com.example.cryptotrack.data.dto.MarketDetailDataDto
import com.example.cryptotrack.data.dto.PriceDto
import com.example.cryptotrack.domain.model.CoinDetails
import com.example.cryptotrack.domain.model.Image
import com.example.cryptotrack.domain.model.Links
import com.example.cryptotrack.domain.model.MarketDetailData
import com.example.cryptotrack.domain.model.Price
import com.example.cryptotrack.domain.model.PriceDate

fun CoinDetailsDto.toDomain() : CoinDetails {
    return CoinDetails(
        id = id,
        symbol = symbol,
        name = name,
        description = description.en ?: "",
        links = links.toDomain(),
        image = image.toDomain(),
        marketData = market_data.toDomain()
    )
}

fun LinksDto.toDomain() : Links {
    return Links(
        homepage = homepage,
        blockchainSite = blockchain_site,
        officialForumUrl = official_forum_url,
        subredditUrl = subreddit_url,
    )
}

fun ImageDto.toDomain() : Image {
    return Image(
        thumb = thumb,
        small = small,
        large = large,
    )
}

fun MarketDetailDataDto.toDomain() : MarketDetailData {
    return MarketDetailData(
        currentPrice = current_price.toDomain(),
        marketCap = market_cap.toDomain(),
        circulatingSupply = circulating_supply,
        totalSupply = total_supply,
        maxSupply = max_supply,
        ath = ath.toDomain(),
        athChangePercentage = ath_change_percentage.toDomain(),
        athDate = ath_date.toDomain(),
        atl = atl.toDomain(),
        atlChangePercentage = atl_change_percentage.toDomain(),
        atlDate = atl_date.toDomain()
    )
}

fun PriceDto.toDomain() : Price {
    return Price(
        usd = usd,
        rub = rub,
    )
}

fun DateDto.toDomain() : PriceDate {
    return PriceDate(
        usd = usd,
        rub = rub,
    )
}


