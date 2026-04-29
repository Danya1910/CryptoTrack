package com.example.cryptotrack.data.repository

import com.example.cryptotrack.data.api.CoinGeckoApi
import com.example.cryptotrack.domain.model.MarketData
import com.example.cryptotrack.data.mapper.toDomain
import com.example.cryptotrack.domain.model.CoinDetails
import com.example.cryptotrack.domain.model.CoinsChartList
import com.example.cryptotrack.domain.model.GlobalMarket
import com.example.cryptotrack.domain.model.Search
import com.example.cryptotrack.domain.model.TrendCoins
import com.example.cryptotrack.domain.repository.CoinGeckoRepository
import retrofit2.http.Query
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

    override suspend fun getCoinDetails(id: String) : CoinDetails {
        return api.getCoinDetails(id = id).toDomain()
    }

    override suspend fun searchCoins(query: String) : Search {
        return api.searchCoins(query = query).toDomain()
    }

    override suspend fun getCoinChart(id: String, vsCurrency: String, days: Int) : CoinsChartList {
        return api.getCoinChart(id = id, vsCurrency = vsCurrency, days = days).toDomain()
    }

    override suspend fun getTrendCoins() : TrendCoins {
        return api.getTrendCoins().toDomain()
    }

}