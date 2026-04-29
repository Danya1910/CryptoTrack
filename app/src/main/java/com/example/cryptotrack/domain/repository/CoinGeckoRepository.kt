package com.example.cryptotrack.domain.repository

import com.example.cryptotrack.data.dto.SearchDto
import com.example.cryptotrack.domain.model.CoinChart
import com.example.cryptotrack.domain.model.CoinDetails
import com.example.cryptotrack.domain.model.CoinsChartList
import com.example.cryptotrack.domain.model.GlobalMarket
import com.example.cryptotrack.domain.model.MarketData
import com.example.cryptotrack.domain.model.Search
import com.example.cryptotrack.domain.model.SearchCoin
import com.example.cryptotrack.domain.model.TrendCoins

interface CoinGeckoRepository {

    suspend fun getGlobalMarket() : GlobalMarket

    suspend fun getMarket() : List<MarketData>

    suspend fun getCoinDetails(id: String) : CoinDetails

    suspend fun searchCoins(query: String) : Search

    suspend fun getCoinChart(id: String, vsCurrency: String, days: Int) : CoinsChartList

    suspend fun getTrendCoins() : TrendCoins

}