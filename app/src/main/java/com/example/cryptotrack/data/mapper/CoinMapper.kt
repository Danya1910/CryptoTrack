package com.example.cryptotrack.data.mapper

import com.example.cryptotrack.data.local.entity.CoinEntity
import com.example.cryptotrack.data.local.entity.FavoriteEntity
import com.example.cryptotrack.data.local.entity.ViewingHistoryEntity
import com.example.cryptotrack.domain.model.FavoriteCoin
import com.example.cryptotrack.domain.model.HistoryOfViewingCoin
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

fun ViewingHistoryEntity.toDomain() : HistoryOfViewingCoin {
    return HistoryOfViewingCoin(
        id = id,
        name = name,
        symbol = symbol,
        imageUrl = imageUrl,
        timestamp =  timestamp,
    )
}

fun HistoryOfViewingCoin.toEntity() : ViewingHistoryEntity {
    return ViewingHistoryEntity(
        id = id,
        name = name,
        symbol = symbol,
        imageUrl = imageUrl,
        timestamp = timestamp,
    )
}

fun FavoriteEntity.toDomain() : FavoriteCoin {
    return FavoriteCoin(
        id = id,
        name = name,
        symbol = symbol,
        imageUrl = imageUrl,
    )
}

fun FavoriteCoin.toEntity() : FavoriteEntity {
    return FavoriteEntity(
        id = id,
        name = name,
        symbol = symbol,
        imageUrl = imageUrl,
    )
}