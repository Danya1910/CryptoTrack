package com.example.cryptotrack.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Grid
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.cryptotrack.R
import com.example.cryptotrack.domain.model.FavoriteCoin
import com.example.cryptotrack.ui.theme.DarkBlue
import com.example.cryptotrack.ui.theme.Green
import com.example.cryptotrack.ui.theme.Inter
import com.example.cryptotrack.ui.theme.OutlineGray


@Composable
fun FavoritesScreen() {

}

@Composable
@Preview(showBackground = true)
private fun FavoritesScreenPreview() {
    Scaffold(
        topBar = {},
        bottomBar = {},
    ) { paddingValues ->
        Content(
            paddingValues = paddingValues,
        )
    }
}

@Composable
private fun Content(
    paddingValues: PaddingValues,
) {

    val favoriteCoins = listOf(
        FavoriteCoin(
            id = "bitcoin",
            name = "Bitcoin",
            symbol = "BTC",
            imageUrl = "https://coin-images.coingecko.com/coins/images/1/thumb/bitcoin.png"
        ),
        FavoriteCoin(
            id = "ethereum",
            name = "Ethereum",
            symbol = "ETH",
            imageUrl = "https://coin-images.coingecko.com/coins/images/279/thumb/ethereum.png"
        ),
        FavoriteCoin(
            id = "tether",
            name = "Tether",
            symbol = "USDT",
            imageUrl = "https://coin-images.coingecko.com/coins/images/325/thumb/Tether.png"
        ),
        FavoriteCoin(
            id = "binancecoin",
            name = "BNB",
            symbol = "BNB",
            imageUrl = "https://coin-images.coingecko.com/coins/images/825/thumb/bnb-icon2_2x.png"
        ),
        FavoriteCoin(
            id = "solana",
            name = "Solana",
            symbol = "SOL",
            imageUrl = "https://coin-images.coingecko.com/coins/images/4128/thumb/solana.png"
        ),
        FavoriteCoin(
            id = "ripple",
            name = "XRP",
            symbol = "XRP",
            imageUrl = "https://coin-images.coingecko.com/coins/images/44/thumb/xrp-symbol-white-128.png"
        ),
        FavoriteCoin(
            id = "cardano",
            name = "Cardano",
            symbol = "ADA",
            imageUrl = "https://coin-images.coingecko.com/coins/images/975/thumb/cardano.png"
        ),
        FavoriteCoin(
            id = "dogecoin",
            name = "Dogecoin",
            symbol = "DOGE",
            imageUrl = "https://coin-images.coingecko.com/coins/images/5/thumb/dogecoin.png"
        ),
        FavoriteCoin(
            id = "polkadot",
            name = "Polkadot",
            symbol = "DOT",
            imageUrl = "https://coin-images.coingecko.com/coins/images/12171/thumb/polkadot.png"
        ),
        FavoriteCoin(
            id = "chainlink",
            name = "Chainlink",
            symbol = "LINK",
            imageUrl = "https://coin-images.coingecko.com/coins/images/877/thumb/chainlink-new-logo.png"
        )
    )

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        FavoriteHat()
        Spacer(modifier = Modifier.height(15.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize()
        ) {
            items(favoriteCoins) { coin ->
                CoinItem(
                    coin = coin,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }


}

@Composable
private fun CoinsList() {

}

@Composable
private fun CoinItem(
    modifier: Modifier,
    coin: FavoriteCoin,
) {
    Box(
        modifier = modifier
            .height(70.dp)
            .background(
                color = DarkBlue,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 1.dp,
                color = OutlineGray,
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 15.dp)
        ) {
            AsyncImage(
                model = coin.imageUrl,
                contentDescription = null,
                modifier = Modifier.size(30.dp)
            )
            Spacer(modifier = Modifier.width(15.dp))
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Text(
                    text = coin.name,
                    fontFamily = Inter,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                )
                Text(
                    text = coin.symbol.uppercase(),
                    fontFamily = Inter,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray,
                )
            }
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_star),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(14.dp)
                )
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_right),
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

@Composable
private fun FavoriteHat() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .background(
                color = DarkBlue,
                shape = RoundedCornerShape(10.dp)
            )
            .border(
                width = 1.dp,
                color = OutlineGray,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(all = 15.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = Color.Black,
                        shape = CircleShape
                    )
                    .padding(all = 10.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_huge_star),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(60.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Text(
                    text = "Избранные монеты",
                    fontFamily = Inter,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                )
                Text(
                    text = "12 монет",
                    fontFamily = Inter,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Green,
                )
                Text(
                    text = "Ваш список любимых криптовалют. \nСледите за ними и не упускайте самое важное.",
                    fontFamily = Inter,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray,
                )
            }
        }
    }
}