package com.example.cryptotrack.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptotrack.domain.model.GlobalMarket
import com.example.cryptotrack.domain.model.TotalMarketCap
import com.example.cryptotrack.domain.model.TotalVolume
import com.example.cryptotrack.domain.usecase.GetGlobalMarketUseCase
import com.example.cryptotrack.domain.usecase.GetMarketUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CoinGeckoViewModel @Inject constructor(
    private val getGlobalMarketUseCase: GetGlobalMarketUseCase,
    private val getMarketUseCase: GetMarketUseCase,
) : ViewModel() {

    val globalMarket = mutableStateOf(
        GlobalMarket(
            activeCryptocurrencies = 0,
            markets = 0,
            marketCapChangePercentage24hUsd = 0.0,
            totalMarketCap = TotalMarketCap(0.0),
            totalVolume = TotalVolume(0.0)
        )
    )


    fun loadGlobalMarket() {
        viewModelScope.launch {
            globalMarket.value = getGlobalMarketUseCase()
        }
    }

}