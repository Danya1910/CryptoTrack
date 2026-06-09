package com.example.cryptotrack.domain.usecase

import com.example.cryptotrack.domain.model.UserData
import com.example.cryptotrack.domain.repository.UserRepository
import javax.inject.Inject

class GetDataUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {

    suspend operator fun invoke() : UserData {
        return userRepository.getData()
    }

}