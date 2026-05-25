package com.example.cryptotrack.domain.repository

import com.example.cryptotrack.domain.model.CoinDetails
import com.example.cryptotrack.domain.model.CoinsChartList
import com.example.cryptotrack.domain.model.GlobalMarket
import com.example.cryptotrack.domain.model.MarketData
import com.example.cryptotrack.domain.model.Search
import com.example.cryptotrack.domain.model.TrendCoins
import com.example.cryptotrack.domain.util.MarketOrder

interface CoinGeckoRepository {

    suspend fun getGlobalMarket() : GlobalMarket

    suspend fun getMarket(order: MarketOrder = MarketOrder.DEFAULT) : List<MarketData>

    suspend fun getCoinDetails(id: String) : CoinDetails

    suspend fun searchCoins(query: String) : Search

    suspend fun getCoinChart(id: String, vsCurrency: String, days: Int) : CoinsChartList

    suspend fun getTrendCoins() : TrendCoins

}