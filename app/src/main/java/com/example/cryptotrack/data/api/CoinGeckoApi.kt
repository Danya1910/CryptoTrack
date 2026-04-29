package com.example.cryptotrack.data.api

import com.example.cryptotrack.data.dto.CoinDetailsDto
import com.example.cryptotrack.data.dto.GlobalMarketResponseDto
import com.example.cryptotrack.data.dto.MarketDataDto
import com.example.cryptotrack.data.dto.CoinChartDto
import com.example.cryptotrack.data.dto.SearchDto
import com.example.cryptotrack.data.dto.TrendCoinsDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CoinGeckoApi {

    @GET(value = "global")
    suspend fun getGlobalMarket() : GlobalMarketResponseDto


    @GET(value = "coins/markets")
    suspend fun getMarket(
        @Query("vs_currency") vsCurrency: String = "usd"
    ) : List<MarketDataDto>

    @GET(value = "coins/{id}")
    suspend fun getCoinDetails(@Path("id") id: String) : CoinDetailsDto

    @GET(value = "search")
    suspend fun searchCoins(
        @Query("query") query: String
    ) : SearchDto

    @GET(value = "coins/{id}/market_chart")
    suspend fun getCoinChart(
        @Path("id") id: String,
        @Query("vs_currency") vsCurrency: String,
        @Query("days") days: Int
    ) : CoinChartDto

    @GET(value = "search/trending")
    suspend fun getTrendCoins() : TrendCoinsDto

}