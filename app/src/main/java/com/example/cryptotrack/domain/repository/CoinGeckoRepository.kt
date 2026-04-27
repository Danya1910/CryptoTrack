package com.example.cryptotrack.domain.repository

import com.example.cryptotrack.domain.model.GlobalMarket
import com.example.cryptotrack.domain.model.MarketData

interface CoinGeckoRepository {

    suspend fun getGlobalMarket() : GlobalMarket

    suspend fun getMarket() : List<MarketData>

}