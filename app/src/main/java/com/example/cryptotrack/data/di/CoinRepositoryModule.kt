package com.example.cryptotrack.data.di

import com.example.cryptotrack.data.local.dao.CoinDao
import com.example.cryptotrack.data.repository.CoinRepositoryImpl
import com.example.cryptotrack.domain.repository.CoinRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CoinRepositoryModule {

    @Provides
    @Singleton
    fun provideRepository(
        dao: CoinDao
    ): CoinRepository {

        return CoinRepositoryImpl(dao)
    }
}