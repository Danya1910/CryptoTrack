package com.example.cryptotrack.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptotrack.domain.usecase.GetCoinChartUseCase
import com.example.cryptotrack.domain.usecase.GetCoinDetailsUseCase
import com.example.cryptotrack.domain.usecase.GetGlobalMarketUseCase
import com.example.cryptotrack.domain.usecase.GetMarketUseCase
import com.example.cryptotrack.domain.usecase.GetTrendCoinsUseCase
import com.example.cryptotrack.domain.util.MarketOrder
import com.example.cryptotrack.presentation.states.DetailsScreenStates
import com.example.cryptotrack.presentation.states.MarketDataState
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
    private val getCoinDetailsUseCase: GetCoinDetailsUseCase,
    private val getCoinChartUseCase: GetCoinChartUseCase,
) : ViewModel() {

    private val _order = MutableStateFlow(MarketOrder.DEFAULT)
    var order = _order.asStateFlow()

    private val _marketScreenState = MutableStateFlow(MarketScreenStates())
    private val _marketDataState = MutableStateFlow(MarketDataState())
    private val _detailScreenState = MutableStateFlow(DetailsScreenStates())
    val marketScreenState = _marketScreenState.asStateFlow()
    val marketDataState = _marketDataState.asStateFlow()
    val detailsScreenState = _detailScreenState.asStateFlow()


    init {
        loadMarketScreen()
        loadMarket(order = MarketOrder.DEFAULT)
    }

    fun loadMarketScreen() {
        viewModelScope.launch {
            _marketScreenState.update {
                it.copy(
                    isLoading = true,
                    error = null,
                )
            }
            runCatching {
                coroutineScope {

                    val globalMarket = async {
                        getGlobalMarketUseCase()
                    }


                    val trend = async {
                        getTrendCoinsUseCase()
                    }

                    Pair(
                        globalMarket.await(),
                        trend.await(),
                    )
                }
            }.onSuccess { (global, trend) ->
                _marketScreenState.update {
                    it.copy(
                        globalMarket = global,
                        trendCoins = trend,
                        isLoading = false,
                        error = null,
                    )
                }
            }.onFailure { throwable ->

                if (throwable is CancellationException) {
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

    fun loadDetails(
        coinId: String
    ) {
        viewModelScope.launch {
            _detailScreenState.update {
                it.copy(
                    isLoading = true,
                    error = null,
                )
            }
            runCatching {
                coroutineScope {
                    val details = async {
                        getCoinDetailsUseCase(id = coinId)
                    }
                    val chart = async {
                        getCoinChartUseCase(id = coinId)
                    }
                    Pair(
                        first = details.await(),
                        second = chart.await()
                    )
                }
            }.onSuccess { (details, chart) ->
                _detailScreenState.update {
                    it.copy(
                        details = details,
                        chart = chart,
                        isLoading = false,
                        error = null,
                    )
                }
            }.onFailure { throwable ->
                if (throwable is CancellationException) {
                    throw throwable
                }
                _detailScreenState.update {
                    it.copy(
                        isLoading = false,
                        error = throwable.message
                    )
                }
            }
        }
    }

    fun changeOrder(
        order: MarketOrder,
    ) {
        _order.value = order

        loadMarket(order = order)
    }

    fun loadMarket(
        order: MarketOrder
    ) {
        viewModelScope.launch {
            _marketDataState.update {
                it.copy(
                    isLoading = true,
                    error = null,
                )
            }
            runCatching {
                getMarketUseCase(order)
            }.onSuccess { market ->
                _marketDataState.update {
                    it.copy(
                        market = market,
                        isLoading = false,
                        error = null,
                    )
                }
            }.onFailure { throwable ->
                if (throwable is CancellationException) {
                    throw throwable
                }

                _marketDataState.update {
                    it.copy(
                        isLoading = false,
                        error = throwable.message
                    )
                }
            }
        }
    }

}