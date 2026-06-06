package com.example.cryptotrack.domain.usecase

import com.example.cryptotrack.domain.model.PurchaseCoin
import com.example.cryptotrack.domain.repository.CoinRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPurchasedCoinsUseCase @Inject constructor(
    private val coinRepository: CoinRepository,
) {

    operator fun invoke() : Flow<List<PurchaseCoin>> {
        return coinRepository.getPurchasedCoins()
    }

}