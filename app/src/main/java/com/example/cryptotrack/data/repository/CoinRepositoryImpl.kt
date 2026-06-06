package com.example.cryptotrack.data.repository

import com.example.cryptotrack.data.local.dao.CoinDao
import com.example.cryptotrack.data.mapper.toDomain
import com.example.cryptotrack.data.mapper.toEntity
import com.example.cryptotrack.domain.model.FavoriteCoin
import com.example.cryptotrack.domain.model.HistoryOfViewingCoin
import com.example.cryptotrack.domain.model.PurchaseCoin
import com.example.cryptotrack.domain.model.RoomCoin
import com.example.cryptotrack.domain.repository.CoinRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CoinRepositoryImpl @Inject constructor(
    private val dao: CoinDao
) : CoinRepository {


    override suspend fun insertCoin(coin: RoomCoin) {
        dao.insetCoin(
            coin = coin.toEntity()
        )
    }

    override  fun getCoins() : Flow<List<RoomCoin>> {
        return dao.getCoins().map { list->
            list.map { entity ->
                entity.toDomain()
            }
        }
    }

    override suspend fun deleteCoin(id: String) {
        dao.deleteCoin(id = id)
    }

    override suspend fun insertCoinToHistoryOfViewing(coin: HistoryOfViewingCoin) {
        val entity = coin.toEntity().copy(
            timestamp = System.currentTimeMillis()
        )

        dao.insertHistoryWithLimit(entity)
    }

    override fun getCoinsFromHistoryOfViewing(): Flow<List<HistoryOfViewingCoin>> {
        return dao.getCoinsFromHistoryOfViewing().map { list->
            list.map { entity ->
                entity.toDomain()
            }
        }
    }

    override suspend fun insertFavoriteCoin(coin: FavoriteCoin) {
        dao.insetFavoriteCoin(coin.toEntity())
    }


    override fun getFavoriteCoins(): Flow<List<FavoriteCoin>> {
        return dao.getFavoriteCoins().map { list->
            list.map { entity ->
                entity.toDomain()
            }
        }
    }

    override suspend fun deleteFavoriteCoin(id: String) {
        dao.deleteFavoriteCoin(id = id)
    }

    override suspend fun deleteAllFavoriteCoins() {
        dao.deleteAllFavoriteCoins()
    }

    override suspend fun insertPurchase(coin: PurchaseCoin) {
        dao.insertPurchase(coin = coin.toEntity())
    }

    override fun getPurchasedCoins(): Flow<List<PurchaseCoin>> {
        return dao.getPurchasedCoins().map { list->
            list.map { entity ->
                entity.toDomain()
            }
        }
    }

    override suspend fun deletePurchasedCoin(coin: PurchaseCoin) {
        dao.deletePurchaseCoin(coin = coin.toEntity())
    }

}
