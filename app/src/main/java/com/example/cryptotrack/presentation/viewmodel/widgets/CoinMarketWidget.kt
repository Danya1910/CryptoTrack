package com.example.cryptotrack.presentation.viewmodel.widgets

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.ModalDrawer
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import com.example.cryptotrack.R
import com.example.cryptotrack.ui.theme.BlackBackground
import com.example.cryptotrack.ui.theme.Inter
import okhttp3.Route


@Composable
fun CoinMarketWidget() {
}


@Composable
@Preview(showBackground = true, backgroundColor = 0xFF292929)
private fun CoinMarketWidgetPreview() {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
        CoinMarketHat()
        CoinsMarketList()
    }
}

@Composable
fun CoinMarketHat(

) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(25.dp)
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = "№",
            fontFamily = Inter,
            fontSize = 12.sp,
            fontWeight = FontWeight.Light,
            color = Color.Gray,
            modifier = Modifier.weight(0.3f),
        )
        Text(
            text = "Название",
            fontFamily = Inter,
            fontSize = 12.sp,
            fontWeight = FontWeight.Light,
            color = Color.Gray,
            modifier = Modifier.weight(2f),
        )
        Text(
            text = "Цена",
            fontFamily = Inter,
            fontSize = 12.sp,
            fontWeight = FontWeight.Light,
            color = Color.Gray,
            modifier = Modifier.weight(0.8f),
        )
        Text(
            text = "24 часа",
            fontFamily = Inter,
            fontSize = 12.sp,
            fontWeight = FontWeight.Light,
            color = Color.Gray,
            modifier = Modifier.weight(0.7f),
        )
    }
}

@Composable
fun CoinMarket() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable {
                //переход на стариницу моенты
            }
            .padding(horizontal = 10.dp)
            .height(30.dp)
            .fillMaxWidth(),
    ) {
        Text(
            text = "1",
            fontFamily = Inter,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White,
            modifier = Modifier.weight(0.3f),
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight()
                .weight(2f),
        ) {
            Icon(
                painter = painterResource(R.drawable.bitcoin),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(25.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Bitcoin",
                fontFamily = Inter,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Btc",
                fontFamily = Inter,
                fontSize = 14.sp,
                fontWeight = FontWeight.Light,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Text(
            textAlign = TextAlign.Left,
            text = "12.35$",
            fontFamily = Inter,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(0.8f)
                .padding(end = 5.dp),
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.7f)
        ) {
            Text(
                text = "1.23%",
                fontFamily = Inter,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Green,
            )
            Spacer(modifier = Modifier.width(3.dp))
            Icon(
                painter = painterResource(R.drawable.ic_up),
                contentDescription = null,
                tint = Color.Unspecified,
            )
        }
    }
}


@Composable
fun CoinsMarketList() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = BlackBackground,
                shape = RoundedCornerShape(30.dp)
            )
            .shadow(
                shape = RoundedCornerShape(30.dp),
                elevation = 2.dp,
                spotColor = Color.White,
            )
            .padding(
                horizontal = 10.dp,
                vertical = 10.dp
            )
    ) {
        Column {
            CoinMarket()
            Spacer(modifier = Modifier.height(5.dp))
            CoinMarket()
            Spacer(modifier = Modifier.height(5.dp))
            CoinMarket()
        }
    }
}