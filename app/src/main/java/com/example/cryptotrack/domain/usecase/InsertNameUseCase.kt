package com.example.cryptotrack.domain.usecase

import com.example.cryptotrack.domain.repository.UserRepository
import javax.inject.Inject

class InsertNameUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {

    suspend operator fun invoke(
        name: String
    ) {
        userRepository.insertName(name = name)
    }

}