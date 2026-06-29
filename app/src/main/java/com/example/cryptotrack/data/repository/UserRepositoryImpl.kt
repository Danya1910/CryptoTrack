package com.example.cryptotrack.data.repository

import com.example.cryptotrack.data.local.dao.UserDao
import com.example.cryptotrack.data.local.entity.UserEntity
import com.example.cryptotrack.domain.model.UserData
import com.example.cryptotrack.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val dao: UserDao,
) : UserRepository {

    override suspend fun insertName(name: String) {
        val current = dao.getUser() ?: UserEntity()

        dao.insert(
            current.copy(name = name)
        )
    }

    override suspend fun insertAvatar(avatar: String) {
        val current = dao.getUser() ?: UserEntity()

        dao.insert(
            current.copy(avatar = avatar)
        )
    }

    override suspend fun getData(): UserData {
        val user = dao.getUser()

        return UserData(
            name = user?.name ?: "",
            avatar = user?.avatar,
        )
    }

}