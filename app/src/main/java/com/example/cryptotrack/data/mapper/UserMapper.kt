package com.example.cryptotrack.data.mapper

import com.example.cryptotrack.data.local.entity.UserEntity
import com.example.cryptotrack.domain.model.UserData

fun UserEntity.toDomain() : UserData {
    return UserData(
        name = name,
        avatar = avatar,
    )
}

fun UserData.toEntity() : UserEntity {
    return UserEntity(
        name = name,
        avatar = avatar,
    )
}
