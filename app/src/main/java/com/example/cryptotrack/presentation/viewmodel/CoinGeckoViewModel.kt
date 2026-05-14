package com.example.cryptotrack.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptotrack.domain.usecase.GetGlobalMarketUseCase
import com.example.cryptotrack.domain.usecase.GetMarketUseCase
import com.example.cryptotrack.domain.usecase.GetTrendCoinsUseCase
import com.example.cryptotrack.presentation.states.MarketScreenStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CoinGeckoViewModel @Inject constructor(
    private val getGlobalMarketUseCase: GetGlobalMarketUseCase,
    private val getMarketUseCase: GetMarketUseCase,
    private val getTrendCoinsUseCase: GetTrendCoinsUseCase,
) : ViewModel() {

    private val _marketScreenState = MutableStateFlow(MarketScreenStates())
    val marketScreenState = _marketScreenState.asStateFlow()


    init{
        loadMarketScreen()
    }

    fun loadMarketScreen() {
        viewModelScope.launch {
            _marketScreenState.update {
                it.copy(
                    isLoading = true
                )
            }
            runCatching {
                coroutineScope {

                    val globalMarket = async {
                        getGlobalMarketUseCase()
                    }

                    val market = async {
                        getMarketUseCase()
                    }

                    val trend = async {
                        getTrendCoinsUseCase()
                    }

                    Triple(
                        globalMarket.await(),
                        market.await(),
                        trend.await(),
                    )
                }
            }.onSuccess { (global, market, trend) ->
                _marketScreenState.update {
                    it.copy(
                        globalMarket = global,
                        market = market,
                        trendCoins = trend,
                        isLoading = false
                    )
                }
            }.onFailure { throwable ->

                if(throwable is CancellationException){
                    throw throwable
                }

                _marketScreenState.update {
                    it.copy(
                        isLoading = false,
                        error = throwable.message
                    )
                }
            }
        }
    }

}