package com.example.cryptotrack.presentation.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.cryptotrack.R
import com.example.cryptotrack.domain.model.CoinChart
import com.example.cryptotrack.domain.model.CoinDetails
import com.example.cryptotrack.domain.model.CoinsChartList
import com.example.cryptotrack.domain.model.Image
import com.example.cryptotrack.domain.model.Links
import com.example.cryptotrack.domain.model.MarketDetailData
import com.example.cryptotrack.domain.model.Price
import com.example.cryptotrack.domain.model.PriceDate
import com.example.cryptotrack.presentation.viewmodel.CoinGeckoViewModel
import com.example.cryptotrack.presentation.widgets.BottomBar
import com.example.cryptotrack.presentation.widgets.BottomBarPreview
import com.example.cryptotrack.presentation.widgets.Graph
import com.example.cryptotrack.presentation.widgets.HistoryGraph
import com.example.cryptotrack.presentation.widgets.TopAppBar
import com.example.cryptotrack.ui.theme.BlackBackground
import com.example.cryptotrack.ui.theme.DarkBlue
import com.example.cryptotrack.ui.theme.Green
import com.example.cryptotrack.ui.theme.Inter
import com.example.cryptotrack.ui.theme.OutlineGray
import com.example.cryptotrack.ui.theme.Red
import com.example.cryptotrack.ui.theme.Yellow
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.stream.IntStream
import kotlin.math.abs


@Composable
fun CoinDetailsScreen(
    viewModel: CoinGeckoViewModel,
    coinId: String,
) {
    Scaffold(
        topBar = {},
        bottomBar = {}
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

    var currentDaysSelected by remember {
        mutableStateOf("24H")
    }

    val days = when (currentDaysSelected) {
        "24H" -> 1
        "7D" -> 7
        "1M" -> 30
        "3M" -> 90
        "1Y" -> 365
        else -> 1
    }

    LaunchedEffect(days) {
        viewModel.loadDetails(coinId = coinId, days = days)
    }

    val state by viewModel.detailsScreenState.collectAsState()

    val details = state.details
    val chart = state.chart

    val symbols = DecimalFormatSymbols().apply {
        groupingSeparator = ' '
        decimalSeparator = '.'
    }

    val formatter = DecimalFormat("#,##0.00", symbols)

    val athValue = details?.marketData?.ath?.usd?.let {
        "$" + formatter.format(it)
    } ?: "0.00"

    val atlValue = details?.marketData?.atl?.usd?.let {
        "$" + formatter.format(it)
    } ?: "0.00"

    val athDate = formatDate(date = details?.marketData?.athDate?.usd)
    val atlDate = formatDate(date = details?.marketData?.atlDate?.usd)

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
        Spacer(modifier = Modifier.height(20.dp))
        DailyPrice(
            low24h = details?.marketData?.low24h?.usd,
            high24h = details?.marketData?.high24h?.usd,
            currentPrice = details?.marketData?.currentPrice?.usd
        )
        Spacer(modifier = Modifier.height(20.dp))
        GraphWrapper(
            currentDaysSelected = currentDaysSelected,
            chart = chart,
            onDaySelected = {
                currentDaysSelected = it
            }
        )
        Spacer(modifier = Modifier.height(10.dp))
        CoinInfo(
            details = details
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            HistoricalGraphItem(
                title = "ATH",
                value = athValue,
                date = athDate,
                percentage = details?.marketData?.athChangePercentage?.usd,
                chart = chart,
                modifier = Modifier.weight(1f)
            )
            HistoricalGraphItem(
                title = "ATL",
                value = atlValue,
                date = atlDate,
                percentage = details?.marketData?.atlChangePercentage?.usd,
                chart = chart,
                modifier = Modifier.weight(1f)
            )

        }
        Spacer(modifier = Modifier.height(10.dp))
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
        abs(details?.marketData?.priceChangePercentage24h ?: 0.0)
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        AsyncImage(
            model = details?.image?.thumb,
            contentDescription = null,
            modifier = Modifier.size(45.dp),
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = details?.name.toString(),
            fontFamily = Inter,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
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
            fontSize = 13.sp,
            fontWeight = FontWeight.Normal,
            color = Color.Gray,
        )
        Spacer(modifier = Modifier.width(10.dp))
        CoinNumber(number = details?.marketCapRank)
        Spacer(modifier = Modifier.weight(1f))
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "$${details?.marketData?.currentPrice?.usd}",
                fontFamily = Inter,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
            )
            Spacer(modifier = Modifier.height(5.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(
                        if (isPositive) R.drawable.ic_up
                        else R.drawable.ic_down
                    ),
                    contentDescription = null,
                    tint = percentageColor,
                    modifier = Modifier.size(7.dp)
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = "$percentageUsd% (24H)",
                    fontFamily = Inter,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = percentageColor,
                )
            }
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
                color = DarkBlue,
                shape = RoundedCornerShape(5.dp),
            )
            .border(
                width = 1.dp,
                color = OutlineGray,
                shape = RoundedCornerShape(5.dp)
            )
            .padding(
                horizontal = 7.dp,
                vertical = 4.dp
            )
    ) {
        Text(
            text = "# $displayNumber",
            fontFamily = Inter,
            fontSize = 10.sp,
            fontWeight = FontWeight.Light,
            color = Color.White,
        )
    }
}

@SuppressLint("ConfigurationScreenWidthHeight")
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

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {

        Column(
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "$$low24h",
                fontFamily = Inter,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start,
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "24H Low",
                fontFamily = Inter,
                fontSize = 10.sp,
                fontWeight = FontWeight.Light,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start,
            )
        }
        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.weight(1f)
        ) {

            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
            ) {

                val barWidth = maxWidth - 20.dp
                val knobOffset = barWidth * progress

                Box(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .fillMaxWidth()
                        .height(5.dp)
                        .align(Alignment.CenterStart)
                        .background(
                            Color(0xFF373737),
                            RoundedCornerShape(10.dp)
                        )
                )

                Box(
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .fillMaxWidth()
                        .height(5.dp)
                        .align(Alignment.CenterStart)
                        .background(
                            brush = Brush.linearGradient(
                                listOf(
                                    Red,
                                    Yellow,
                                    Green,
                                )
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )
                )

                Box(
                    modifier = Modifier
                        .offset(x = 10.dp + knobOffset - 6.dp)
                        .align(Alignment.CenterStart)
                        .size(12.dp)
                        .background(
                            Color.White,
                            CircleShape
                        )
                )
            }
            Spacer(modifier = Modifier.height(13.dp))
            Box(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth()
                    .drawBehind {
                        drawLine(
                            color = Color.Gray,
                            start = Offset(0f, size.height / 2),
                            end = Offset(size.width, size.height / 2),
                            strokeWidth = 1.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(
                                intervals = floatArrayOf(
                                    12f,
                                    6f
                                )
                            )
                        )
                    }
            )
        }
        Column(
            verticalArrangement = Arrangement.Top,
        ) {
            Text(
                text = "$$high24h",
                fontFamily = Inter,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.End,
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "24H High",
                fontFamily = Inter,
                fontSize = 10.sp,
                fontWeight = FontWeight.Light,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.End,
            )
        }
    }
}

@Composable
private fun GraphWrapper(
    chart: CoinsChartList?,
    currentDaysSelected: String,
    onDaySelected: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = DarkBlue,
                shape = RoundedCornerShape(10.dp)
            )
            .border(
                color = OutlineGray,
                width = 1.dp,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(all = 10.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            DaysSelectionWidget(
                currentDaysSelected = currentDaysSelected,
                onDaySelected = onDaySelected,
            )
            Graph(
                chart = chart,
            )
        }
    }
}

@Composable
private fun DaysSelectionWidget(
    currentDaysSelected: String,
    onDaySelected: (String) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .height(20.dp)
            .fillMaxWidth()
    ) {
        listOf("24H", "7D", "1M", "3M", "1Y").forEach { day ->

            DayItem(
                title = day,
                currentDaysSelected = currentDaysSelected,
                modifier = Modifier.weight(1f),
                onClick = {
                    onDaySelected(day)
                }
            )
        }
    }
}

@Composable
private fun DayItem(
    modifier: Modifier,
    title: String,
    onClick: () -> Unit,
    currentDaysSelected: String,
) {
    val isSelected = currentDaysSelected == title


    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxHeight()
            .padding(horizontal = 10.dp)
            .background(
                color = if (isSelected) Green.copy(alpha = 0.4f) else Color.Transparent,
                shape = RoundedCornerShape(10.dp)
            )
    ) {
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = if (isSelected) Green else Color.Gray,
            fontFamily = Inter,
            modifier = Modifier
                .clickable {
                    onClick()
                }
        )
    }
}

@Composable
private fun CoinInfo(
    details: CoinDetails?,
) {

    val marketCap = formatCompactNumber(number = details?.marketData?.marketCap?.usd)

    val fullyDilutedValuation =
        formatCompactNumber(number = details?.marketData?.fullyDilutedValuation?.usd)

    val totalVolume = formatCompactNumber(number = details?.marketData?.totalVolume?.usd)

    val circulatingSupply = formatCompactNumber(number = details?.marketData?.circulatingSupply)

    val totalSupply = formatCompactNumber(number = details?.marketData?.totalSupply)

    val maxSupply = formatCompactNumber(number = details?.marketData?.maxSupply)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = DarkBlue,
                shape = RoundedCornerShape(10.dp)
            )
            .border(
                color = OutlineGray,
                width = 1.dp,
                shape = RoundedCornerShape(10.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 15.dp)
        ) {
            Text(
                text = "Основные показатели",
                fontFamily = Inter,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White,
                modifier = Modifier
                    .padding(horizontal = 10.dp)
            )
            Spacer(modifier = Modifier.height(15.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(IntrinsicSize.Min),
            ) {
                InfoItem(
                    icon = R.drawable.ic_market_cap,
                    title = "Market Cap",
                    value = "$$marketCap",
                    modifier = Modifier.weight(1f),
                )
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                        .background(color = OutlineGray)
                )
                InfoItem(
                    icon = R.drawable.ic_stack,
                    title = "Fully Diluted Valuation",
                    value = "$$fullyDilutedValuation",
                    modifier = Modifier.weight(1f),
                )
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                        .background(color = OutlineGray)
                )
                InfoItem(
                    icon = R.drawable.ic_planet,
                    title = "Volume (24h)",
                    value = "$$totalVolume",
                    modifier = Modifier.weight(1f),
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .height(1.dp)
                    .background(color = OutlineGray)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(IntrinsicSize.Min),
            ) {
                InfoItem(
                    icon = R.drawable.ic_market_cap,
                    title = "Circulating Supply",
                    value = "$circulatingSupply ${details?.symbol?.uppercase()}",
                    modifier = Modifier.weight(1f),
                )
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                        .background(color = OutlineGray)
                )
                InfoItem(
                    icon = R.drawable.ic_stack,
                    title = "Total supply",
                    value = "$totalSupply ${details?.symbol?.uppercase()}",
                    modifier = Modifier.weight(1f),
                )
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                        .background(color = OutlineGray)
                )
                InfoItem(
                    icon = R.drawable.ic_planet,
                    title = "Max Supply",
                    value = "$maxSupply ${details?.symbol?.uppercase()}",
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
private fun InfoItem(
    icon: Int,
    title: String,
    value: String,
    modifier: Modifier,
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            fontFamily = Inter,
            fontSize = 9.sp,
            fontWeight = FontWeight.Normal,
            color = Color.Gray,
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = value,
            fontFamily = Inter,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White,
        )
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

fun formatDate(date: String?): String {
    if (date == null) return ""

    val instant = Instant.parse(date)

    val formatter = DateTimeFormatter.ofPattern(
        "d MMM yyyy",
        Locale("ru")
    )

    return formatter.format(
        instant.atZone(ZoneId.systemDefault())
    )
}

@SuppressLint("DefaultLocale")
@Composable
private fun HistoricalGraphItem(
    title: String,
    value: String,
    date: String,
    percentage: Double?,
    chart: CoinsChartList?,
    modifier: Modifier
) {


    val percentageFormatted = String.format(
        "%.2f",
        abs(percentage ?: 0.0)
    )

    val percentageColor = if (percentage ?: 0.0 >= 0.0) Green else Red


    val percentageText = if ((percentage ?: 0.0) >= 0) {
        "+$percentageFormatted%"
    } else {
        "-$percentageFormatted%"
    }

    Box(
        modifier = modifier
            .background(
                color = DarkBlue,
                shape = RoundedCornerShape(10.dp)
            )
            .border(
                color = OutlineGray,
                width = 1.dp,
                shape = RoundedCornerShape(10.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = title,
                    fontFamily = Inter,
                    fontSize = 10.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Normal,
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = value,
                    fontFamily = Inter,
                    fontSize = 12.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Normal,
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = date,
                    fontFamily = Inter,
                    fontSize = 10.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Normal,
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = percentageText,
                    fontFamily = Inter,
                    fontSize = 11.sp,
                    color = percentageColor ?: Color.Gray,
                    fontWeight = FontWeight.Normal,
                )
            }
            Spacer(modifier = Modifier.width(15.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 60.dp, height = 50.dp)
                ) {
                    HistoryGraph(chart = chart)
                }
            }
        }
    }
}

@Composable
private fun CommunityBlock(
    images: Image?,
    links: Links?,
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            LinkRow(
                path = images?.thumb,
                title = "Оффициальный сайт",
                link = links?.homepage?.firstOrNull(),
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(color = OutlineGray)
            )
            LinkRow(
                image = R.drawable.ic_reddit,
                title = "Reddit",
                link = links?.subredditUrl
            )
        }
    }
}

@Composable
private fun LinkRow(
    image: Int? = null,
    path: String? = null,
    title: String,
    link: String?,
) {

    val context = LocalContext.current


    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(40.dp)
            .fillMaxWidth()
            .clickable {
                if (!link.isNullOrEmpty()) {

                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(link)
                    )

                    context.startActivity(intent)
                }
            }
            .padding(horizontal = 10.dp)
    ) {
        if (image == null) {
            AsyncImage(
                model = path,
                contentDescription = null,
                modifier = Modifier.size(25.dp),
            )
        } else {
            Icon(
                painter = painterResource(id = image),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(25.dp)
            )
        }
        Spacer(modifier = Modifier.width(15.dp))
        Text(
            text = title,
            textAlign = TextAlign.Start,
            fontFamily = Inter,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White,
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            painter = painterResource(id = R.drawable.ic_link),
            contentDescription = null,
            tint = Green,
            modifier = Modifier
                .size(15.dp)
        )
    }
}
