package com.example.cryptotrack.data.mapper

import com.example.cryptotrack.data.dto.FavoriteCoinDetailsDto
import com.example.cryptotrack.domain.model.FavoriteCoinDetails

fun FavoriteCoinDetailsDto.toDomain() : FavoriteCoinDetails {
    return FavoriteCoinDetails(
        id = id,
        symbol = symbol,
        name = name,
        image = image,
        currentPrice = current_price,
        priceChangePercentage24h = price_change_percentage_24h,
    )
}