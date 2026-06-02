package com.example.cryptotrack.domain.usecase

import com.example.cryptotrack.domain.model.HistoryOfViewingCoin
import com.example.cryptotrack.domain.repository.CoinRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCoinsFromHistoryOfViewingUseCase @Inject constructor(
    private val coinRepository: CoinRepository,
) {

    operator fun invoke() : Flow<List<HistoryOfViewingCoin>>{
        return coinRepository.getCoinsFromHistoryOfViewing()
    }

}