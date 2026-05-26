package com.example.cryptotrack.domain.repository

import com.example.cryptotrack.domain.model.RoomCoin
import kotlinx.coroutines.flow.Flow

interface CoinRepository {

    suspend fun insertCoin(coin: RoomCoin)

    fun getCoins() : Flow<List<RoomCoin>>

    suspend fun deleteCoin(id: String)

}