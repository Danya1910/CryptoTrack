package com.example.cryptotrack.domain.usecase

import com.example.cryptotrack.domain.model.GlobalMarket
import com.example.cryptotrack.domain.repository.CoinGeckoRepository
import javax.inject.Inject

class GetGlobalMarketUseCase @Inject constructor(
    private val repository: CoinGeckoRepository
) {

    suspend operator fun invoke() : GlobalMarket {
        return repository.getGlobalMarket()
    }

}