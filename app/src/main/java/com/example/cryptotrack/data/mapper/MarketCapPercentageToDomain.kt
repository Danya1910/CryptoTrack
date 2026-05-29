package com.example.cryptotrack.data.mapper

import com.example.cryptotrack.data.dto.MarketCapPercentageDto
import com.example.cryptotrack.domain.model.MarketCapPercentage

fun MarketCapPercentageDto.toDomain() : MarketCapPercentage {
    return MarketCapPercentage(
        btc = btc,
        eth = eth,
    )
}