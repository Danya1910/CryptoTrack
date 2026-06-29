package com.example.cryptotrack.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptotrack.domain.model.Search
import com.example.cryptotrack.domain.usecase.GetCoinChartUseCase
import com.example.cryptotrack.domain.usecase.GetCoinDetailsUseCase
import com.example.cryptotrack.domain.usecase.GetFavoriteCoinsDetailsUseCase
import com.example.cryptotrack.domain.usecase.GetGlobalMarketUseCase
import com.example.cryptotrack.domain.usecase.GetMarketUseCase
import com.example.cryptotrack.domain.usecase.GetTrendCoinsUseCase
import com.example.cryptotrack.domain.usecase.SearchCoinsUseCase
import com.example.cryptotrack.domain.util.MarketOrder
import com.example.cryptotrack.presentation.states.ChartState
import com.example.cryptotrack.presentation.states.DetailsOfFoundCoin
import com.example.cryptotrack.presentation.states.DetailsState
import com.example.cryptotrack.presentation.states.FavoriteCoinsDetailsState
import com.example.cryptotrack.presentation.states.GlobalMarketState
import com.example.cryptotrack.presentation.states.MarketDataState
import com.example.cryptotrack.presentation.states.SearchState
import com.example.cryptotrack.presentation.states.TrendState
import com.example.cryptotrack.presentation.util.price.aggregatePurchases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject


@HiltViewModel
class CoinGeckoViewModel @Inject constructor(
    private val getGlobalMarketUseCase: GetGlobalMarketUseCase,
    private val getMarketUseCase: GetMarketUseCase,
    private val getTrendCoinsUseCase: GetTrendCoinsUseCase,
    private val getCoinDetailsUseCase: GetCoinDetailsUseCase,
    private val getCoinChartUseCase: GetCoinChartUseCase,
    private val searchCoinsUseCase: SearchCoinsUseCase,
    private val getFavoriteCoinsDetailsUseCase: GetFavoriteCoinsDetailsUseCase,
) : ViewModel() {

    private val _order = MutableStateFlow(MarketOrder.DEFAULT)
    var order = _order.asStateFlow()

    private val _globalMarketState = MutableStateFlow(GlobalMarketState())
    private val _marketDataState = MutableStateFlow(MarketDataState())
    private val _detailsState = MutableStateFlow(DetailsState())
    private val _chartState = MutableStateFlow(ChartState())
    private val _trendState = MutableStateFlow(TrendState())
    private val _searchState = MutableStateFlow(SearchState())
    private val _favoriteCoinsDetailsState = MutableStateFlow(FavoriteCoinsDetailsState())
    private val _foundCoinDetailsState = MutableStateFlow(DetailsOfFoundCoin())
    val marketScreenState = _globalMarketState.asStateFlow()
    val marketDataState = _marketDataState.asStateFlow()
    val detailsState = _detailsState.asStateFlow()
    val chartState = _chartState.asStateFlow()
    val trendState = _trendState.asStateFlow()
    val searchState = _searchState.asStateFlow()
    val favoriteCoinsDetailsState = _favoriteCoinsDetailsState.asStateFlow()
    val foundCoinDetailsState = _foundCoinDetailsState.asStateFlow()

    private val _isSplashReady = MutableStateFlow(false)
    val isSplashReady = _isSplashReady.asStateFlow()


    init {
        loadMarket(order = MarketOrder.DEFAULT)
        loadSplash()
    }

    fun loadSplash() {
        viewModelScope.launch {
            loadGlobalMarket()
            loadTrends()
            loadMarket(order = MarketOrder.DEFAULT)

            withTimeoutOrNull(5000) {
                val minimumDelayJob = launch { delay(3000) }
                val dataLoadingJob = launch {
                    combine(
                        _globalMarketState,
                        _trendState,
                        _marketDataState
                    ) { global, trends, market ->
                        val isGlobalReady = global.globalMarket != null || global.error != null
                        val isTrendsReady = trends.trendCoins != null || trends.error != null
                        val isMarketReady = market.market != null || market.error != null

                        isGlobalReady && isTrendsReady && isMarketReady
                    }.first { it }
                }
                minimumDelayJob.join()
                dataLoadingJob.join()
            }
            _isSplashReady.value = true
        }
    }

    fun loadGlobalMarket() {
        if (_globalMarketState.value.globalMarket != null || _globalMarketState.value.isLoading) return
        viewModelScope.launch {
            _globalMarketState.update {
                it.copy(
                    isLoading = true,
                    error = null,
                )
            }
            runCatching {
                coroutineScope {

                    getGlobalMarketUseCase()

                }
            }.onSuccess { globalMarket ->
                _globalMarketState.update {
                    it.copy(
                        globalMarket = globalMarket,
                        isLoading = false,
                        error = null,
                    )
                }
            }.onFailure { throwable ->

                if (throwable is CancellationException) {
                    throw throwable
                }

                _globalMarketState.update {
                    it.copy(
                        isLoading = false,
                        error = throwable.message
                    )
                }
            }
        }
    }

    fun loadTrends() {
        if (_trendState.value.trendCoins != null || _trendState.value.isLoading) return
        viewModelScope.launch {
            _trendState.update {
                it.copy(
                    isLoading = true
                )
            }
            runCatching {
                getTrendCoinsUseCase()
            }.onSuccess { trend ->
                _trendState.update {
                    it.copy(
                        isLoading = false,
                        trendCoins = trend
                    )
                }
            }.onFailure { throwable ->
                if (throwable is CancellationException) {
                    throw throwable
                }
                _trendState.update {
                    it.copy(
                        isLoading = false,
                        error = throwable.message
                    )
                }
            }
        }
    }

    fun loadDetails(
        coinId: String,
    ) {
        viewModelScope.launch {
            _detailsState.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    details = null,
                )
            }
            runCatching {
                getCoinDetailsUseCase(id = coinId)
            }.onSuccess { details ->
                _detailsState.update {
                    it.copy(
                        details = details,
                        isLoading = false,
                        error = null,
                    )
                }
            }.onFailure { throwable ->
                if (throwable is CancellationException) {
                    throw throwable
                }
                _detailsState.update {
                    it.copy(
                        isLoading = false,
                        error = throwable.message
                    )
                }
            }
        }
    }

    fun loadChart(
        coinId: String,
        days: Int = 1,
    ) {
        viewModelScope.launch {
            _chartState.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    chart = null
                )
            }
            runCatching {
                    getCoinChartUseCase(id = coinId, days = days)
            }.onSuccess { chart ->
                _chartState.update {
                    it.copy(
                        chart = chart,
                        isLoading = false,
                        error = null,
                    )
                }
            }.onFailure { throwable ->
                if (throwable is CancellationException) {
                    throw throwable
                }
                _chartState.update {
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
        if (!_marketDataState.value.market.isNullOrEmpty() || _marketDataState.value.isLoading) return
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

    fun search(
        query: String
    ) {
        viewModelScope.launch {
            _searchState.update {
                it.copy(
                    isLoading = true
                )
            }
            runCatching {
                searchCoinsUseCase(query = query)
            }.onSuccess { suggestions ->
                _searchState.update {
                    it.copy(
                        isLoading = false,
                        suggestions = suggestions,
                    )
                }
            }.onFailure { throwable ->
                if (throwable is CancellationException) {
                    throw throwable
                }
                _searchState.update {
                    it.copy(
                        isLoading = false,
                        error = throwable.message,
                    )
                }
            }
        }
    }

    fun getFavoriteCoinsDetails(
        ids: String
    ) {

        val listIds = ids.split(",").map { it.trim() }


        viewModelScope.launch {
            val currentDetails = _favoriteCoinsDetailsState.value.details
            val loadedIds = currentDetails?.map { it.id }?.toSet() ?: emptySet()
            val isEverythingLoaded = loadedIds.containsAll(listIds)

            if (isEverythingLoaded) return@launch

            _favoriteCoinsDetailsState.update {
                it.copy(
                    isLoading = true,
                    //details = emptyList()
                )
            }
            runCatching {
                getFavoriteCoinsDetailsUseCase(ids = ids)
            }.onSuccess { details ->
                _favoriteCoinsDetailsState.update {
                    it.copy(
                        isLoading = false,
                        details = details,
                        error = null,
                    )
                }
            }.onFailure { throwable ->
                if (throwable is CancellationException) {
                    throw throwable
                }
                _favoriteCoinsDetailsState.update {
                    it.copy(
                        isLoading = false,
                        error = throwable.message,
                    )
                }
            }
        }
    }

    fun getFoundCoinDetails(
        id: String
    ) {

        val trimmedId = id.trim()


        viewModelScope.launch {
            val currentDetails = _favoriteCoinsDetailsState.value.details

            val existingCoin = currentDetails?.find { it.id == trimmedId }

            if (existingCoin != null) {
                _foundCoinDetailsState.update { it.copy(details = existingCoin, isLoading = false) }
                return@launch
            }

            _foundCoinDetailsState.update {
                it.copy(
                    isLoading = true,
                )
            }

            runCatching {
                getFavoriteCoinsDetailsUseCase(ids = id)
            }.onSuccess { details ->
                val newCoin = details.firstOrNull()
                _foundCoinDetailsState.update {
                    it.copy(
                        isLoading = false,
                        details = newCoin,
                        error = null,
                    )
                }
                if(newCoin != null) {
                    _favoriteCoinsDetailsState.update { state ->
                        val updatedList = (state.details ?: emptyList()) + newCoin
                        state.copy(
                            details = updatedList
                        )
                    }
                }
            }.onFailure { throwable ->
                if (throwable is CancellationException) {
                    throw throwable
                }
                _foundCoinDetailsState.update {
                    it.copy(
                        isLoading = false,
                        error = throwable.message,
                    )
                }
            }
        }
    }

    fun observeAndFetchDetails(
        coinViewModel: CoinViewModel,
    ) {
        viewModelScope.launch {
            combine(
                coinViewModel.favoriteCoins,
                coinViewModel.purchase,
                _favoriteCoinsDetailsState
            ) { favoriteList, purchaseList, currentDetailsState ->
                val aggregated = aggregatePurchases(purchaseList)
                val purchaseIds = aggregated.map { it.coinId }
                val favoriteIds = favoriteList.map { it.id }
                val allRequiredIds = (purchaseIds + favoriteIds).toSet()

                val loadedIds = currentDetailsState.details?.map { it.id }?.toSet() ?: emptySet()

                val isEverythingLoaded = loadedIds.containsAll(allRequiredIds)

                Pair(allRequiredIds, isEverythingLoaded)
            }
                .filter { (allRequiredIds, _) -> allRequiredIds.isNotEmpty() }
                .collect { (allUniqueIds, isEverythingLoaded) ->
                    if (isEverythingLoaded) return@collect

                    val idsString = allUniqueIds.joinToString(",")
                    while (true) {
                        getFavoriteCoinsDetails(ids = idsString)
                        delay(1500)
                        val currentDetails = _favoriteCoinsDetailsState.value.details
                        if (!currentDetails.isNullOrEmpty()) break
                        delay(8500)
                    }
                }
        }
    }

    fun clearSuggestionsList() {
        _searchState.update {
            it.copy(
                suggestions = Search(
                    coins = emptyList()
                )
            )
        }
    }

    fun clearFoundCoin() {
        _foundCoinDetailsState.update {
            it.copy(
                details = null,
                error = null,
            )
        }
    }
}