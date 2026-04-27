package com.example.cryptotrack.domain.usecase

import com.example.cryptotrack.domain.model.CoinDetails
import com.example.cryptotrack.domain.repository.CoinGeckoRepository
import javax.inject.Inject

class GetCoinDetailsUseCase @Inject constructor(
    private val repository: CoinGeckoRepository
){

    suspend operator fun invoke(
        id: String
    ) : CoinDetails {
        return repository.getCoinDetails(id = id)
    }

}