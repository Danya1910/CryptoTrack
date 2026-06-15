package com.example.cryptotrack.presentation.widgets

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cryptotrack.R
import com.example.cryptotrack.domain.model.GlobalMarket
import com.example.cryptotrack.domain.model.MarketCapPercentage
import com.example.cryptotrack.domain.model.TotalMarketCap
import com.example.cryptotrack.domain.model.TotalVolume
import com.example.cryptotrack.presentation.util.price.formatWithSpaces
import com.example.cryptotrack.presentation.util.price.toCompactUsd
import com.example.cryptotrack.presentation.util.something.shimmer
import com.example.cryptotrack.ui.theme.DarkBlue
import com.example.cryptotrack.ui.theme.Green
import com.example.cryptotrack.ui.theme.Inter
import com.example.cryptotrack.ui.theme.OutlineGray
import com.example.cryptotrack.ui.theme.Red
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import kotlin.math.abs


@SuppressLint("DefaultLocale")
@Composable
fun GlobalMarketWidget(
    market: GlobalMarket?,
) {

    val isPositive =
        market?.marketCapChangePercentage24hUsd?.let {
            if (it >= 0) true
            else false
        }

    val percentageColor = if (isPositive == true) Green else Red

    val marketCap = String.format(
        "%.2f",
        market?.marketCapChangePercentage24hUsd
    )

    val marketCapColor = if ((market?.marketCapChangePercentage24hUsd ?: 0.0) >= 0.0) Green else Red


    val formattedPercentageBtc = String.format(
        "%.2f",
        market?.marketCapPercentage?.btc,
    )

    val formattedPercentageEth = String.format(
        "%.2f",
        market?.marketCapPercentage?.eth,
    )

    val symbolsNum = DecimalFormatSymbols().apply {
        groupingSeparator = ' '
    }

    val formatterNum = DecimalFormat("#,###", symbolsNum)

    val activeCoins = market?.activeCryptocurrencies.formatWithSpaces(formatterNum)
    val markets = market?.markets.formatWithSpaces(formatterNum)

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
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 15.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = marketCapColor.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(10.dp)
                        )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_internet),
                        contentDescription = null,
                        tint = marketCapColor,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(modifier = Modifier.width(15.dp))
                Column(
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = "Global Market Cap",
                        fontFamily = Inter,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = market?.totalMarketCap?.usd?.toCompactUsd() ?: "",
                        fontFamily = Inter,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(
                                if (isPositive == true) R.drawable.ic_up
                                else R.drawable.ic_down
                            ),
                            contentDescription = null,
                            tint = percentageColor,
                            modifier = Modifier.size(7.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "$marketCap % (24h)",
                            fontFamily = Inter,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = percentageColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Active Coins",
                        fontFamily = Inter,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = activeCoins,
                        fontFamily = Inter,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Markets",
                        fontFamily = Inter,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = markets,
                        fontFamily = Inter,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(color = OutlineGray)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 15.dp)
            ) {
                GlobalMarketItem(
                    title = "Market Cap",
                    value = market?.totalMarketCap?.usd?.toCompactUsd() ?: "",
                    percentage = market?.marketCapChangePercentage24hUsd ?: 0.0
                )
                Box(
                    modifier = Modifier
                        .height(60.dp)
                        .width(1.dp)
                        .background(color = OutlineGray)
                )
                GlobalMarketItem(
                    title = "24h volume",
                    value = market?.totalVolume?.usd?.toCompactUsd() ?: "",
                    percentage = market?.volumeChangePercentage24Usd
                )
                Box(
                    modifier = Modifier
                        .height(60.dp)
                        .width(1.dp)
                        .background(color = OutlineGray)
                )
                GlobalMarketItem(
                    title = "BTC Dominance",
                    value = "$formattedPercentageBtc%",
                )
                Box(
                    modifier = Modifier
                        .height(60.dp)
                        .width(1.dp)
                        .background(color = OutlineGray)
                )
                GlobalMarketItem(
                    title = "ETH Dominance",
                    value = "$formattedPercentageEth%",
                )

            }

        }
    }
}

@Composable
fun GlobalMarketWidgetSkeleton(
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
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 15.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = Color.Transparent,
                            shape = RoundedCornerShape(10.dp)
                        )
                ) {
                    SkeletonBox(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(
                                shape = RoundedCornerShape(10.dp)
                            )
                    )
                    Icon(
                        painter = painterResource(R.drawable.ic_internet),
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(modifier = Modifier.width(15.dp))
                Column(
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = "Global Market Cap",
                        fontFamily = Inter,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    SkeletonBox(
                        modifier = Modifier
                            .width(45.dp)
                            .height(16.dp)
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    SkeletonBox(
                        modifier = Modifier
                            .width(70.dp)
                            .height(13.dp)
                    )
                }
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Active Coins",
                        fontFamily = Inter,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    SkeletonBox(
                        modifier = Modifier
                            .width(50.dp)
                            .height(13.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Markets",
                        fontFamily = Inter,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    SkeletonBox(
                        modifier = Modifier
                            .width(50.dp)
                            .height(13.dp)
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(color = OutlineGray)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 15.dp)
            ) {
                GlobalMarketItemSkeleton(
                    title = "Market Cap",
                )
                Box(
                    modifier = Modifier
                        .height(60.dp)
                        .width(1.dp)
                        .background(color = OutlineGray)
                )
                GlobalMarketItemSkeleton(
                    title = "24h volume",
                )
                Box(
                    modifier = Modifier
                        .height(60.dp)
                        .width(1.dp)
                        .background(color = OutlineGray)
                )
                GlobalMarketItemSkeleton(
                    title = "BTC Dominance",
                )
                Box(
                    modifier = Modifier
                        .height(60.dp)
                        .width(1.dp)
                        .background(color = OutlineGray)
                )
                GlobalMarketItemSkeleton(
                    title = "ETH Dominance",
                )

            }
        }
    }
}


@SuppressLint("DefaultLocale")
@Composable
@Preview(showBackground = true, backgroundColor = 0xFF292929)
private fun GlobalMarketWidgetPreview() {

    val market = GlobalMarket(
        activeCryptocurrencies = 17234,
        markets = 842,
        marketCapChangePercentage24hUsd = 2.45,
        volumeChangePercentage24Usd = -9.32412,
        totalMarketCap = TotalMarketCap(
            usd = 2_640_000_000_000.0
        ),
        totalVolume = TotalVolume(
            usd = 124_500_000_000.0
        ),
        marketCapPercentage = MarketCapPercentage(
            btc = 57.6142953935747,
            eth = 9.51393678147639,
        )
    )

    val isPositive =
        market.marketCapChangePercentage24hUsd.let {
            if (it >= 0) true
            else false
        }

    val percentageColor = if (isPositive) Green else Red

    val marketCap = String.format(
        "%.2f",
        market.marketCapChangePercentage24hUsd
    )

    val marketCapColor = if ((market.marketCapChangePercentage24hUsd) >= 0.0) Green else Red


    val formattedPercentageBtc = String.format(
        "%.2f",
        market.marketCapPercentage.btc,
    )

    val formattedPercentageEth = String.format(
        "%.2f",
        market.marketCapPercentage.eth,
    )

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
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 15.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = marketCapColor.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(10.dp)
                        )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_internet),
                        contentDescription = null,
                        tint = marketCapColor,
                        modifier = Modifier
                            .size(28.dp)
                            .shimmer()
                    )
                }
                Spacer(modifier = Modifier.width(15.dp))
                Column(
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = "Global Market Cap",
                        fontFamily = Inter,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = market.totalMarketCap.usd.toCompactUsd(),
                        fontFamily = Inter,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
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
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "$marketCap % (24h)",
                            fontFamily = Inter,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = percentageColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Active Coins",
                        fontFamily = Inter,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = "17,542",
                        fontFamily = Inter,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Markets",
                        fontFamily = Inter,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = "1434",
                        fontFamily = Inter,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(color = OutlineGray)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 15.dp)
            ) {
                GlobalMarketItem(
                    title = "Market Cap",
                    value = market.totalMarketCap.usd.toCompactUsd(),
                    percentage = market.marketCapChangePercentage24hUsd
                )
                Box(
                    modifier = Modifier
                        .height(60.dp)
                        .width(1.dp)
                        .background(color = OutlineGray)
                )
                GlobalMarketItem(
                    title = "24h volume",
                    value = market.totalVolume.usd.toCompactUsd(),
                    percentage = market.volumeChangePercentage24Usd
                )
                Box(
                    modifier = Modifier
                        .height(60.dp)
                        .width(1.dp)
                        .background(color = OutlineGray)
                )
                GlobalMarketItem(
                    title = "BTC Dominance",
                    value = "$formattedPercentageBtc%",
                )
                Box(
                    modifier = Modifier
                        .height(60.dp)
                        .width(1.dp)
                        .background(color = OutlineGray)
                )
                GlobalMarketItem(
                    title = "ETH Dominance",
                    value = "$formattedPercentageEth%",
                )

            }

        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
private fun GlobalMarketItem(
    title: String,
    value: String,
    percentage: Double? = null,
) {

    val absPercentage = percentage?.let { abs(it) }

    val isPositive =
        percentage?.let {
            if (it >= 0) true
            else false
        }

    val percentageColor = if (isPositive == true) Green else Red

    val formattedPercents = String.format(
        "%.2f",
        absPercentage,
    )



    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = title,
            fontFamily = Inter,
            fontSize = 9.sp,
            fontWeight = FontWeight.Normal,
            color = Color.Gray,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(
            modifier = Modifier.height(
                if (percentage == null) 20.dp else 10.dp
            )
        )
        Text(
            text = value,
            fontFamily = Inter,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(
            modifier = Modifier.height(
                if (percentage == null) 12.dp else 0.dp
            )
        )
        if (percentage != null) {
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(
                        if (isPositive == true) R.drawable.ic_up
                        else R.drawable.ic_down
                    ),
                    contentDescription = null,
                    tint = percentageColor,
                    modifier = Modifier.size(7.dp)
                )

                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = "$formattedPercents%",
                    fontFamily = Inter,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = percentageColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun GlobalMarketItemSkeleton(
    title: String,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = title,
            fontFamily = Inter,
            fontSize = 9.sp,
            fontWeight = FontWeight.Normal,
            color = Color.Gray,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(
            modifier = Modifier.height(
                if (title.contains("Dominance")) 20.dp else 10.dp
            )
        )
        SkeletonBox(
            modifier = Modifier
                .width(40.dp)
                .height(13.dp)
        )
        Spacer(
            modifier = Modifier.height(
                if (title.contains("Dominance")) 12.dp else 0.dp
            )
        )
        if (!title.contains("Dominance")) {
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                SkeletonBox(
                    modifier = Modifier
                        .width(40.dp)
                        .height(13.dp)
                )
            }
        }
    }
}