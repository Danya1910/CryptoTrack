package com.example.cryptotrack.presentation.screens

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.cryptotrack.R
import com.example.cryptotrack.domain.model.FavoriteCoinDetails
import com.example.cryptotrack.domain.model.PurchaseCoin
import com.example.cryptotrack.presentation.util.price.aggregatePurchases
import com.example.cryptotrack.presentation.util.price.formatPrice
import com.example.cryptotrack.presentation.util.uiModels.AggregatedPurchase
import com.example.cryptotrack.presentation.viewmodel.CoinGeckoViewModel
import com.example.cryptotrack.presentation.viewmodel.CoinViewModel
import com.example.cryptotrack.presentation.widgets.BottomBar
import com.example.cryptotrack.presentation.widgets.PurchaseTopAppBar
import com.example.cryptotrack.ui.theme.BlackBackground
import com.example.cryptotrack.ui.theme.DarkBlue
import com.example.cryptotrack.ui.theme.Green
import com.example.cryptotrack.ui.theme.Inter
import com.example.cryptotrack.ui.theme.OutlineGray


@Composable
fun PurchaseScreen(
    navController: NavController,
    coinViewModel: CoinViewModel,
    viewModel: CoinGeckoViewModel,
) {
    Scaffold(
        topBar = {
            PurchaseTopAppBar(
                navController = navController,
            )
        },
        bottomBar = {
            BottomBar(
                navController = navController
            )
        },
    ) { paddingValues ->
        Content(
            paddingValues = paddingValues,
            navController = navController,
            coinViewModel = coinViewModel,
            viewModel = viewModel,
        )
    }
}

@Composable
private fun Content(
    paddingValues: PaddingValues,
    navController: NavController,
    coinViewModel: CoinViewModel,
    viewModel: CoinGeckoViewModel,
) {

    val purchase by coinViewModel.purchase.collectAsState(initial = emptyList())

    val aggregatedPurchase = remember(purchase) {
        aggregatePurchases(purchase)
    }

    LaunchedEffect(aggregatedPurchase) {
        if (purchase.isNotEmpty()) {
            val ids = aggregatedPurchase.joinToString(",") { it.coinId }
            viewModel.getFavoriteCoinsDetails(ids = ids)
        }
    }
    
    val purchaseDetails by viewModel.favoriteCoinsDetailsState.collectAsState()

    val details = purchaseDetails.details


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = BlackBackground
            )
            .padding(paddingValues)
    ) {
        TotalVolume()



        CoinsList(
            purchase = aggregatedPurchase,
            details = details,
        )
    }
}

@Composable
private fun TotalVolume(

) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = DarkBlue,
                shape = RoundedCornerShape(10.dp)
            )
            .border(
                width = 1.dp,
                color = OutlineGray,
                shape = RoundedCornerShape(10.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 15.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
            ) {
                Text(
                    text = "Общая стоимость",
                    fontFamily = Inter,
                    color = Color.Gray,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "1 245 735,45 $",
                    fontFamily = Inter,
                    color = Color.White,
                    fontWeight = FontWeight.Normal,
                    fontSize = 20.sp,
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "+12.45%",
                        fontFamily = Inter,
                        color = Green,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "за все время",
                        fontFamily = Inter,
                        color = Color.White,
                        fontWeight = FontWeight.Normal,
                        fontSize = 10.sp,
                    )
                }
            }
        }
    }
}


@Composable
private fun ListItem(
    purchase: AggregatedPurchase,
    details: FavoriteCoinDetails,
) {

    val totalPrice = formatPrice(purchase.totalValue)
    val percentage = details.priceChangePercentage24h


    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(55.dp)
            .fillMaxWidth()
            .background(color = DarkBlue)
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(0.7f)
        ) {
            AsyncImage(
                model = details.image,
                contentDescription = null,
                modifier = Modifier.size(30.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = details.name,
                    fontFamily = Inter,
                    color = Color.White,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "${purchase.totalAmount} BTC",
                    fontFamily = Inter,
                    color = Color.Gray,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                )
            }
        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End,
        ) {
            Text(
                text = "$totalPrice$",
                fontFamily = Inter,
                color = Color.White,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "$percentage%",
                fontFamily = Inter,
                color = Color.Green,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
            )
        }
    }
}

@Composable
private fun CoinsList(
    purchase: List<AggregatedPurchase>,
    details: List<FavoriteCoinDetails>?,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(1f)
            .fillMaxHeight(0.5f)
            .background(color = Color.Gray)
    ) {
        purchase.take(5).forEach { item ->
            val coinDetails = details?.find { it.id == item.coinId }

            if (coinDetails != null) {
                ListItem(
                    purchase = item,
                    details = coinDetails
                )
            }
        }
    }
}

@Composable
private fun ListHat() {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Мои активы",
            fontFamily = Inter,
            color = Color.White,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "Ну тут фильтр",
            textAlign = TextAlign.End,
            fontFamily = Inter,
            color = Color.Gray,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
        )
    }
}