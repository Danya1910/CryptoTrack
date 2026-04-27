package com.example.cryptotrack.data.mapper

import com.example.cryptotrack.data.dto.TotalVolumeDto
import com.example.cryptotrack.domain.model.TotalVolume

fun TotalVolumeDto.toDomain() : TotalVolume {
    return TotalVolume(
        usd = usd ?: 0.0
    )
}