package com.example.cryptotrack.presentation.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.cryptotrack.R
import com.example.cryptotrack.domain.model.CoinDetails
import com.example.cryptotrack.domain.model.CoinsChartList
import com.example.cryptotrack.domain.model.Image
import com.example.cryptotrack.domain.model.Links
import com.example.cryptotrack.presentation.viewmodel.CoinGeckoViewModel
import com.example.cryptotrack.presentation.widgets.BottomBarPreview
import com.example.cryptotrack.presentation.widgets.Graph
import com.example.cryptotrack.presentation.widgets.TopAppBar
import com.example.cryptotrack.ui.theme.BlackBackground
import com.example.cryptotrack.ui.theme.Green
import com.example.cryptotrack.ui.theme.Inter
import com.example.cryptotrack.ui.theme.Red
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols


@Composable
fun CoinDetailsScreen(
    viewModel: CoinGeckoViewModel,
    coinId: String,
) {
    Scaffold(
        topBar = {
            TopAppBar()
        },
        bottomBar = {
            BottomBarPreview()
        }
    ) { paddingValues ->
        Content(
            paddingValues = paddingValues,
            viewModel = viewModel,
            coinId = coinId,
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun CoinDetailsScreenPreview() {

//    Scaffold(
//        topBar = {
//            TopAppBar()
//        },
//        bottomBar = {
//            BottomBarPreview()
//        }
////    ) { paddingValues ->
////        Content(
////            paddingValues = paddingValues,
////        )
//    }

}

@Composable
private fun Content(
    paddingValues: PaddingValues,
    viewModel: CoinGeckoViewModel,
    coinId: String,
) {


    LaunchedEffect(Unit) {
        viewModel.loadDetails(coinId = coinId)
    }

    val state by viewModel.detailsScreenState.collectAsState()

    val details = state.details
    val chart = state.chart

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = BlackBackground
            )
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 15.dp)
            .padding(paddingValues)
    ) {
        CoinHat(
            details = details
        )
        DailyPrice(
            low24h = details?.marketData?.low24h?.usd,
            high24h = details?.marketData?.high24h?.usd,
            currentPrice = details?.marketData?.currentPrice?.usd
        )
        Spacer(modifier = Modifier.height(20.dp))
        GraphWrapper(
            chart = chart,
        )
        Spacer(modifier = Modifier.height(20.dp))
        CoinInfo(
            details = details
        )
        Spacer(modifier = Modifier.height(20.dp))
        CommunityBlock(
            images = details?.image,
            links = details?.links,
        )
    }
}

@SuppressLint("DefaultLocale")
@Composable
private fun CoinHat(
    details: CoinDetails?
) {

    val isPositive =
        (details?.marketData?.priceChangePercentage24h ?: 0.0) >= 0

    val percentageColor =
        if (isPositive) Green
        else Red

    val percentageUsd = String.format(
        "%.1f",
        kotlin.math.abs(details?.marketData?.priceChangePercentage24h ?: 0.0)
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        AsyncImage(
            model = details?.image?.thumb,
            contentDescription = null,
            modifier = Modifier.size(40.dp),
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = details?.name.toString(),
            fontFamily = Inter,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            letterSpacing = 1.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.widthIn(max = 120.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = details?.symbol.toString(),
            fontFamily = Inter,
            fontSize = 20.sp,
            fontWeight = FontWeight.Light,
            color = Color.Gray,
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = "Price",
            fontFamily = Inter,
            fontSize = 20.sp,
            fontWeight = FontWeight.Light,
            color = Color.Gray,

            )
        Spacer(modifier = Modifier.width(10.dp))
        CoinNumber(number = details?.marketCapRank)
    }
    Spacer(modifier = Modifier.height(25.dp))
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Text(
            text = "${details?.marketData?.currentPrice?.usd} $",
            fontFamily = Inter,
            fontSize = 28.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            letterSpacing = 1.sp,
        )
        Spacer(modifier = Modifier.width(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(bottom = 10.dp)
        ) {
            Icon(
                painter = painterResource(
                    if (isPositive) R.drawable.ic_up
                    else R.drawable.ic_down
                ),
                contentDescription = null,
                tint = percentageColor,
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = "$percentageUsd % (24H)",
                fontFamily = Inter,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = percentageColor,
                letterSpacing = 1.sp,
            )
        }
    }
}

@Composable
private fun CoinNumber(
    number: Int?
) {
    val displayNumber = number?.let {
        if (it >= 1000000) {
            "1M+"
        } else if (number >= 100000) {
            "100K+"

        } else if (number >= 10000) {
            "9999+"

        } else {
            number.toString()
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .height(22.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(12.dp),
            )
            .shadow(
                elevation = 8.dp,
                spotColor = Color.White,
                shape = RoundedCornerShape(12.dp),
            )
            .padding(
                horizontal = 7.dp,
                vertical = 4.dp
            )
    ) {
        Text(
            text = "№ $displayNumber",
            fontFamily = Inter,
            fontSize = 12.sp,
            fontWeight = FontWeight.Light,
            color = Color.Black,
        )
    }
}

@Composable
private fun DailyPrice(
    low24h: Double?,
    high24h: Double?,
    currentPrice: Double?,
) {

    val low = low24h?.toFloat() ?: 0f
    val high = high24h?.toFloat() ?: 0f
    val current = currentPrice?.toFloat() ?: 0f

    val range = (high - low).takeIf { it > 0f } ?: 1f

    val progress = ((current - low) / range)
        .coerceIn(0f, 1f)

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .height(8.dp)
                .background(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFF373737)
                )
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .height(8.dp)
                    .background(
                        shape = RoundedCornerShape(10.dp),
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFFEEFF00),
                                Color(0xFF00FF51),
                            )
                        )
                    )
                    .fillMaxWidth(progress)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 15.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "$$low24h",
                fontFamily = Inter,
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = "24h",
                fontFamily = Inter,
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = "$$high24h",
                fontFamily = Inter,
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun GraphWrapper(
    chart: CoinsChartList?,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                shape = RoundedCornerShape(30.dp),
                elevation = 4.dp,
                spotColor = Color.White,
            )
            .background(
                color = BlackBackground,
                shape = RoundedCornerShape(30.dp)
            )
            .padding(all = 10.dp)
    ) {
        Graph(
            chart = chart,
        )
    }
}

@Composable
private fun CoinInfo(
    details: CoinDetails?,
) {

    val symbols = DecimalFormatSymbols().apply {
        groupingSeparator = ' '
        decimalSeparator = '.'
    }

    val formatterInteger = DecimalFormat("#,##0", symbols)

    val totalVolume = details?.marketData?.totalVolume?.usd?.let {
        formatterInteger.format(it)
    } ?: "0.00"

    val totalSupply = details?.marketData?.totalSupply?.let {
        formatterInteger.format(it)
    } ?: "0.00"

    val marketCap = details?.marketData?.marketCap?.usd?.let {
        formatterInteger.format(it)
    } ?: "0.00"

    val circulatingSupply = formatCompactNumber(number = details?.marketData?.circulatingSupply)

    val maxSupply = details?.marketData?.maxSupply?.let {
        formatterInteger.format(it)
    } ?: "0.00"

    val fullyDilutedValuation = details?.marketData?.fullyDilutedValuation?.usd?.let {
        formatterInteger.format(it)
    } ?: "0.00"







    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "Информация",
            fontFamily = Inter,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White,
            modifier = Modifier
                .padding(horizontal = 20.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    shape = RoundedCornerShape(30.dp),
                    elevation = 4.dp,
                    spotColor = Color.White,
                )
                .background(
                    color = BlackBackground,
                    shape = RoundedCornerShape(30.dp)
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 15.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()

                ) {
                    Text(
                        text = "Рыночная капитализация",
                        textAlign = TextAlign.Start,
                        fontFamily = Inter,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "Количество в обращении",
                        textAlign = TextAlign.Start,
                        fontFamily = Inter,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 3.dp)
                ) {
                    Text(
                        text = "$marketCap $",
                        textAlign = TextAlign.Start,
                        fontFamily = Inter,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = circulatingSupply,
                        textAlign = TextAlign.Start,
                        fontFamily = Inter,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()

                ) {
                    Text(
                        text = "Полностью разбавленная оценка",
                        textAlign = TextAlign.Start,
                        fontFamily = Inter,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "Общее предложение",
                        textAlign = TextAlign.Start,
                        fontFamily = Inter,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 3.dp)
                ) {
                    Text(
                        text = "$fullyDilutedValuation $",
                        textAlign = TextAlign.Start,
                        fontFamily = Inter,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "$totalSupply $",
                        textAlign = TextAlign.Start,
                        fontFamily = Inter,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        modifier = Modifier.weight(1f)

                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()

                ) {
                    Text(
                        text = "Максимальное предложение",
                        textAlign = TextAlign.Start,
                        fontFamily = Inter,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "Объем торгов",
                        textAlign = TextAlign.Start,
                        fontFamily = Inter,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 3.dp)
                ) {
                    Text(
                        text = "$maxSupply $",
                        textAlign = TextAlign.Start,
                        fontFamily = Inter,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "$totalVolume $",
                        textAlign = TextAlign.Start,
                        fontFamily = Inter,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        modifier = Modifier.weight(1f)

                    )
                }
            }
        }
    }
}

@SuppressLint("DefaultLocale")
fun formatCompactNumber(
    number: Double?
): String {

    if (number != null) {
        return when {

            number >= 1_000_000_000_000 -> {
                String.format("%.1fT+", number / 1_000_000_000_000)
            }

            number >= 1_000_000_000 -> {
                String.format("%.1fB+", number / 1_000_000_000)
            }

            number >= 1_000_000 -> {
                String.format("%.1fM+", number / 1_000_000)
            }

            number >= 1_000 -> {
                String.format("%.1fK+", number / 1_000)
            }

            else -> {
                number.toInt().toString()
            }
        }
    }
    return number.toString()
}

@Composable
private fun CommunityBlock(
    images: Image?,
    links: Links?,
) {

    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable {

                    val url = links?.homepage?.firstOrNull()

                    if (!url.isNullOrBlank()) {

                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(url)
                        )

                        context.startActivity(intent)
                    }
                }
        ) {
            AsyncImage(
                model = images?.thumb,
                contentDescription = null,
                modifier = Modifier.size(25.dp),
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Официальный сайт",
                textAlign = TextAlign.Start,
                fontFamily = Inter,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White,
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable {
                    val url = links?.subredditUrl

                    if (!url.isNullOrBlank()) {

                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(url)
                        )

                        context.startActivity(intent)
                    }
                }
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_reddit),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(30.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Reddit",
                textAlign = TextAlign.Start,
                fontFamily = Inter,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White,
            )
        }
    }
}
