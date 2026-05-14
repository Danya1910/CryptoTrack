package com.example.cryptotrack.presentation.screens

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
import com.example.cryptotrack.presentation.widgets.BottomBarPreview
import com.example.cryptotrack.presentation.widgets.CoinMarketWidget
import com.example.cryptotrack.presentation.widgets.GlobalMarketWidget
import com.example.cryptotrack.presentation.widgets.TrendCoinsWidget
import com.example.cryptotrack.ui.theme.BlackBackground


@Composable
fun MarketScreen(
    viewModel: CoinGeckoViewModel
) {

    Scaffold(
        topBar = {},
        containerColor = BlackBackground,
        bottomBar = {
            BottomBarPreview()
        }
    ) { paddingValues ->
        Content(
            viewModel = viewModel,
            paddingValues = paddingValues,)
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
) {

    LaunchedEffect(Unit) {
        viewModel.loadMarketScreen()
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(paddingValues = paddingValues)
            .padding(horizontal = 15.dp)
            .fillMaxSize()
            .background(color = BlackBackground)
    ) {
        GlobalMarketWidget()
        Spacer(modifier = Modifier.height(20.dp))
        TrendCoinsWidget()
        Spacer(modifier = Modifier.height(20.dp))
        CoinMarketWidget()
    }
}