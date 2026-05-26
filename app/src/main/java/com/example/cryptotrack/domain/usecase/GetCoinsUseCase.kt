package com.example.cryptotrack.domain.usecase

import com.example.cryptotrack.domain.model.RoomCoin
import com.example.cryptotrack.domain.repository.CoinRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCoinsUseCase @Inject constructor(
    private val coinRepository: CoinRepository,
) {

    operator fun invoke(): Flow<List<RoomCoin>> {
        return coinRepository.getCoins()
    }

}