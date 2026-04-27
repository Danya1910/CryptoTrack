package com.example.cryptotrack.data.mapper

import com.example.cryptotrack.data.dto.MarketDataDto
import com.example.cryptotrack.domain.model.MarketData

fun MarketDataDto.toDomain() : MarketData {
    return MarketData(
        id = id,
        symbol = symbol,
        name = name,
        image = image,
        currentPrice = current_price ?: 0.0,
        marketCap = market_cap ?: 0.0,
        marketCapRank = market_cap_rank ?: 0,
        lastUpdated = last_updated ?: "",
        priceChange24h = price_change_24h ?: 0.0
    )
}