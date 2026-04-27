package com.example.cryptotrack.data.api

import android.icu.util.Currency
import com.example.cryptotrack.data.dto.GlobalMarketDto
import com.example.cryptotrack.data.dto.GlobalMarketResponseDto
import com.example.cryptotrack.data.dto.MarketDataDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CoinGeckoApi {

    @GET(value = "global")
    suspend fun getGlobalMarket() : GlobalMarketResponseDto


    @GET(value = "coins/markets")
    suspend fun getMarket(
        @Query("vs_currency") vsCurrency: String = "usd"
    ) : List<MarketDataDto>

}