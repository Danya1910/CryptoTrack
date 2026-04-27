package com.example.cryptotrack.data.repository

import com.example.cryptotrack.data.api.CoinGeckoApi
import com.example.cryptotrack.domain.model.MarketData
import com.example.cryptotrack.data.mapper.toDomain
import com.example.cryptotrack.domain.model.GlobalMarket
import com.example.cryptotrack.domain.repository.CoinGeckoRepository
import javax.inject.Inject

class CoinGeckoRepositoryImpl @Inject constructor(
    private val api: CoinGeckoApi
) : CoinGeckoRepository {

    override suspend fun getGlobalMarket(): GlobalMarket {
        return api.getGlobalMarket().toDomain()
    }

    override suspend fun getMarket() : List<MarketData> {
        return api.getMarket().map { it.toDomain() }
    }

}