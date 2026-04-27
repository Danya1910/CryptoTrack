package com.example.cryptotrack.data.di

import com.example.cryptotrack.data.repository.CoinGeckoRepositoryImpl
import com.example.cryptotrack.domain.repository.CoinGeckoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindCoinGeckoRepository(
        impl: CoinGeckoRepositoryImpl
    ): CoinGeckoRepository

}