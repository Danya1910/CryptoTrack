package com.example.cryptotrack.data.mapper

import android.provider.Settings
import com.example.cryptotrack.data.dto.GlobalMarketResponseDto
import com.example.cryptotrack.domain.model.GlobalMarket

fun GlobalMarketResponseDto.toDomain() : GlobalMarket {
    return data.toDomain()
}