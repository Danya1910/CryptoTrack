package com.example.cryptotrack.domain.repository

import com.example.cryptotrack.domain.model.UserData

interface UserRepository {

    suspend fun insertName(name: String)

    suspend fun insertAvatar(avatar: String)

    suspend fun getData() : UserData

}