package com.example.cryptotrack.data.mapper

import com.example.cryptotrack.data.dto.TotalMarketCapDto
import com.example.cryptotrack.domain.model.TotalMarketCap

fun TotalMarketCapDto.toDomain() : TotalMarketCap {
    return TotalMarketCap(
        usd = usd ?: 0.0
    )
}