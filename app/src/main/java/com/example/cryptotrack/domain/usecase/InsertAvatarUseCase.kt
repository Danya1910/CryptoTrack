package com.example.cryptotrack.domain.usecase

import com.example.cryptotrack.domain.repository.UserRepository
import javax.inject.Inject

class InsertAvatarUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {

    suspend operator fun invoke(
        avatar: String
    ) {
        userRepository.insertAvatar(avatar = avatar)
    }

}