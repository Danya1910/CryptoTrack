package com.example.cryptotrack.data.mapper

import com.example.cryptotrack.data.dto.CoinChartDto
import com.example.cryptotrack.domain.model.CoinChart
import com.example.cryptotrack.domain.model.CoinsChartList

fun CoinChartDto.toDomain(): CoinsChartList {
    return CoinsChartList(
        list = prices.mapNotNull {
            if (it.size >= 2) {
                CoinChart(
                    time = it[0].toLong(),
                    price = it[1]
                )
            } else null
        }
    )
}