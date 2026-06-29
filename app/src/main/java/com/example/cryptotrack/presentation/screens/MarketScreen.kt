package com.example.cryptotrack.presentation.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cryptotrack.presentation.viewmodel.CoinGeckoViewModel
import com.example.cryptotrack.presentation.widgets.CoinMarketWidget
import com.example.cryptotrack.presentation.widgets.GlobalMarketWidget
import com.example.cryptotrack.ui.theme.BlackBackground
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.example.cryptotrack.domain.util.MarketOrder
import com.example.cryptotrack.presentation.viewmodel.CoinViewModel
import com.example.cryptotrack.presentation.widgets.BottomBar
import com.example.cryptotrack.presentation.widgets.CoinMarketWidgetSkeleton
import com.example.cryptotrack.presentation.widgets.GlobalMarketWidgetSkeleton
import com.example.cryptotrack.presentation.widgets.TrendWidget
import com.example.cryptotrack.presentation.widgets.TrendWidgetSkeleton


@Composable
fun MarketScreen(
    viewModel: CoinGeckoViewModel,
    navController: NavController,
    coinViewModel: CoinViewModel,
) {

    Scaffold(
        topBar = {
        },
        containerColor = BlackBackground,
        bottomBar = {
            BottomBar(navController = navController)
        }
    ) { paddingValues ->
        Content(
            viewModel = viewModel,
            paddingValues = paddingValues,
            navController = navController,
            coinViewModel = coinViewModel,
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun MarketScreenPreview() {

//    Scaffold(
//        topBar = {},
//        containerColor = BlackBackground,
//        bottomBar = {
//            BottomBarPreview()
//        }
//    ) { paddingValues ->
//        //Content(paddingValues = paddingValues)
//    }

}

@Composable
private fun Content(
    paddingValues: PaddingValues,
    viewModel: CoinGeckoViewModel,
    navController: NavController,
    coinViewModel: CoinViewModel,
) {

    val order by viewModel.order.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadGlobalMarket()
        viewModel.loadTrends()
        viewModel.loadMarket(order = MarketOrder.DEFAULT)
        Log.d("MarketScreen", "${coinViewModel.coins}")
    }


    val screenState by viewModel.marketScreenState.collectAsState()
    val marketData by viewModel.marketDataState.collectAsState()
    val trendCoins by viewModel.trendState.collectAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(paddingValues = paddingValues)
            .padding(horizontal = 15.dp)
            .fillMaxSize()
            .background(color = BlackBackground)
    ) {
        if(screenState.isLoading || screenState.globalMarket == null) {
            GlobalMarketWidgetSkeleton()
        }
        else {
            GlobalMarketWidget(
                market = screenState.globalMarket
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        if(trendCoins.isLoading || trendCoins.trendCoins == null){
            TrendWidgetSkeleton()
        } else {
            TrendWidget(
                trends = trendCoins.trendCoins,
                navController = navController,
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        if(marketData.isLoading || marketData.market.isNullOrEmpty()){
            CoinMarketWidgetSkeleton(
                order = order,
                viewModel = viewModel,
            )
        }
        else {
            CoinMarketWidget(
                order = order,
                coins = marketData.market,
                viewModel = viewModel,
                navController = navController,
                coinViewModel = coinViewModel,
            )
        }
        Spacer(modifier = Modifier.height(20.dp))

    }
}