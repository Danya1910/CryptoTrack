package com.example.cryptotrack.data.mapper

import com.example.cryptotrack.data.dto.SearchCoinDto
import com.example.cryptotrack.data.dto.SearchDto
import com.example.cryptotrack.domain.model.Search
import com.example.cryptotrack.domain.model.SearchCoin

fun SearchDto.toDomain() : Search {
    return Search(
        coins = coins.map { it.toDomain() }
    )
}

fun SearchCoinDto.toDomain() : SearchCoin {
    return SearchCoin(
        id = id,
        name = name,
        apiSymbol = api_symbol,
        symbol = symbol,
        marketCapRank = market_cap_rank,
        thumb = thumb,
        large = large
    )
}