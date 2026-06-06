package com.example.cryptotrack.domain.repository

import com.example.cryptotrack.domain.model.FavoriteCoin
import com.example.cryptotrack.domain.model.HistoryOfViewingCoin
import com.example.cryptotrack.domain.model.PurchaseCoin
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

    // Избранные
    suspend fun insertFavoriteCoin(coin: FavoriteCoin)

    fun getFavoriteCoins() : Flow<List<FavoriteCoin>>

    suspend fun deleteFavoriteCoin(id: String)

    suspend fun deleteAllFavoriteCoins()

    //purchase
    suspend fun insertPurchase(coin: PurchaseCoin)

    fun getPurchasedCoins() : Flow<List<PurchaseCoin>>

    suspend fun deletePurchasedCoin(coin: PurchaseCoin)

}