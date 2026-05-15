package com.example.cryptotrack.presentation.widgets

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.cryptotrack.R
import com.example.cryptotrack.domain.model.TrendCoin
import com.example.cryptotrack.domain.model.TrendCoins
import com.example.cryptotrack.ui.theme.BlackBackground
import com.example.cryptotrack.ui.theme.Green
import com.example.cryptotrack.ui.theme.Inter
import com.example.cryptotrack.ui.theme.Red
import kotlinx.serialization.StringFormat


@Composable
fun TrendCoinsWidget(
    trends: TrendCoins?
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
            .padding(
                horizontal = 10.dp,
                vertical = 10.dp
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = "Популярно \uD83D\uDD25",
                    fontFamily = Inter,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Полностью",
                    fontFamily = Inter,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable {}
                )
                Spacer(modifier = Modifier.width(20.dp))
            }
            Spacer(modifier = Modifier.height(10.dp))

            trends?.coins?.take(3)?.forEach { coin ->
                CoinIcon(
                    coin = coin
                )
            }
        }
    }

}


@Composable
@Preview(showBackground = true, backgroundColor = 0xFF292929)
private fun TrendCoinsWidgetPreview() {
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
            .padding(
                horizontal = 10.dp,
                vertical = 10.dp
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = "Популярно \uD83D\uDD25",
                    fontFamily = Inter,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Полностью",
                    fontFamily = Inter,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable {}
                )
                Spacer(modifier = Modifier.width(20.dp))
            }
            Spacer(modifier = Modifier.height(10.dp))
//            CoinIcon()
//            CoinIcon()
//            CoinIcon()
        }
    }

}

@SuppressLint("DefaultLocale")
@Composable
private fun CoinIcon(
    coin: TrendCoin?
) {

    val isPositive =
        (coin?.data?.priceChangePercentage24h?.usd ?: 0.0) >= 0.0

    val percentageColor =
        if (isPositive) Green
        else Red

    val percentageUsd = String.format(
        "%.1f",
        kotlin.math.abs(coin?.data?.priceChangePercentage24h?.usd ?: 0.0)
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .clickable {
                //Навишация на подпобное описание монеты
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = coin?.coinId.toString(),
                fontFamily = Inter,
                fontSize = 14.sp,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(0.3f)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(2f)
            ) {


                AsyncImage(
                    model = coin?.thumb,
                    contentDescription = null,
                    modifier = Modifier.size(25.dp),
                )
                Spacer(modifier = Modifier.width(15.dp))
                Text(
                    text = coin?.name ?: "Unknown",
                    fontFamily = Inter,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                    letterSpacing = 1.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.7f)
            ) {
                Text(
                    text = "$percentageUsd %",
                    fontFamily = Inter,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = percentageColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.width(3.dp))
                Icon(
                    painter = painterResource(
                        if (isPositive) R.drawable.ic_up
                        else R.drawable.ic_down
                    ),
                    contentDescription = null,
                    tint = percentageColor,
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
        }
    }
}