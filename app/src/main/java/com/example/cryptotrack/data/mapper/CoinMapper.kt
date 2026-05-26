package com.example.cryptotrack.data.mapper

import com.example.cryptotrack.data.local.entity.CoinEntity
import com.example.cryptotrack.domain.model.RoomCoin

fun CoinEntity.toDomain() : RoomCoin {
    return RoomCoin(
        id = id,
        name = name,
        path = path,
    )
}

fun RoomCoin.toEntity() : CoinEntity {
    return CoinEntity(
        id = id,
        name = name,
        path = path,
    )
}