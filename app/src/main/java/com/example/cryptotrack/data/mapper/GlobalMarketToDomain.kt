package com.example.cryptotrack.data.mapper

import com.example.cryptotrack.data.dto.GlobalMarketDto
import com.example.cryptotrack.domain.model.GlobalMarket

fun GlobalMarketDto.toDomain() : GlobalMarket {
    return GlobalMarket(
        activeCryptocurrencies = active_cryptocurrencies,
        markets = markets,
        marketCapChangePercentage24hUsd = market_cap_change_percentage_24h_usd,
        totalMarketCap = total_market_cap.toDomain(),
        totalVolume = total_volume.toDomain()
    )
}