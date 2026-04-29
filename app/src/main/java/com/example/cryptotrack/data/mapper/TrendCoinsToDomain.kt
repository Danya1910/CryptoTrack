package com.example.cryptotrack.data.mapper

import com.example.cryptotrack.data.dto.PriceChangeDto
import com.example.cryptotrack.data.dto.TrendCoinDto
import com.example.cryptotrack.data.dto.TrendCoinsDto
import com.example.cryptotrack.data.dto.TrendDataDto
import com.example.cryptotrack.data.dto.TrendItemDto
import com.example.cryptotrack.domain.model.PriceChange
import com.example.cryptotrack.domain.model.TrendCoin
import com.example.cryptotrack.domain.model.TrendCoins
import com.example.cryptotrack.domain.model.TrendData

fun TrendCoinsDto.toDomain(): TrendCoins {
    return TrendCoins(
        coins = coins.map { it.toDomain() }
    )
}

fun TrendItemDto.toDomain(): TrendCoin {
    return item.toDomain()
}

fun TrendCoinDto.toDomain(): TrendCoin {
    return TrendCoin(
        id = id,
        coinId = coin_id,
        name = name,
        symbol = symbol,
        marketCapRank = market_cap_rank,
        thumb = thumb,
        data = data.toDomain()
    )
}

fun TrendDataDto.toDomain(): TrendData {
    return TrendData(
        price = price,
        priceChangePercentage24h = price_change_percentage_24h.toDomain()
    )
}

fun PriceChangeDto.toDomain(): PriceChange {
    return PriceChange(
        usd = usd,
        rub = rub ?: 0.0
    )
}