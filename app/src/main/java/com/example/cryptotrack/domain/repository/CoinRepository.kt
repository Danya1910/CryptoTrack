package com.example.cryptotrack.domain.repository

import com.example.cryptotrack.domain.model.HistoryOfViewingCoin
import com.example.cryptotrack.domain.model.RoomCoin
import kotlinx.coroutines.flow.Flow

interface CoinRepository {


    // История поиска
    suspend fun insertCoin(coin: RoomCoin)

    fun getCoins() : Flow<List<RoomCoin>>

    suspend fun deleteCoin(id: String)


    // История просмотра монет
    suspend fun insertCoinToHistoryOfViewing(coin: HistoryOfViewingCoin)

    fun getCoinsFromHistoryOfViewing() : Flow<List<HistoryOfViewingCoin>>


}