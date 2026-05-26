package com.example.cryptotrack.data.repository

import com.example.cryptotrack.data.local.dao.CoinDao
import com.example.cryptotrack.data.mapper.toDomain
import com.example.cryptotrack.data.mapper.toEntity
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

}