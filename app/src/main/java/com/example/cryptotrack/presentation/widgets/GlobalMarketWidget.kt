package com.example.cryptotrack.presentation.widgets

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cryptotrack.ui.theme.Inter
import com.example.cryptotrack.R


@Composable
fun GlobalMarketWidget() {
    val text = "123"
    val iconId = "inlineIcon"

    val annotatedString = buildAnnotatedString {
        append("Монет: ")
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.White)) {
            append(text)
        }
        append(", Бирж: 228, Рост рынка: 2% ")

        // 2. Резервируем место под иконку
        appendInlineContent(iconId, "[icon]")

        append(", Капитализация: ")
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.White)) {
            append(text)
        }
    }

    val inlineContent = mapOf(
        iconId to InlineTextContent(
            Placeholder(
                width = 16.sp,
                height = 16.sp,
                placeholderVerticalAlign = PlaceholderVerticalAlign.Center,
            )
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_up),
                contentDescription = null,
                tint = Color.Unspecified,
            )
        }
    )
    Box(
        modifier = Modifier
            .height(25.dp)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .horizontalScroll(
                    rememberScrollState()
                )
        ) {
            Text(
                text = annotatedString,
                inlineContent = inlineContent,
                fontFamily = Inter,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray,
                softWrap = false,
                maxLines = 1,
            )
        }
    }
}


@Composable
@Preview(showBackground = true, backgroundColor = 0xFF292929)
private fun GlobalMarketWidgetPreview() {
    val text = "123"
    val iconId = "inlineIcon"

    val annotatedString = buildAnnotatedString {
        append("Монет: ")
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.White)) {
            append(text)
        }
        append(", Бирж: 228, Рост рынка: 2% ")

        // 2. Резервируем место под иконку
        appendInlineContent(iconId, "[icon]")

        append(", Капитализация: ")
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.White)) {
            append(text)
        }
    }

    val inlineContent = mapOf(
        iconId to InlineTextContent(
            Placeholder(
                width = 16.sp,
                height = 16.sp,
                placeholderVerticalAlign = PlaceholderVerticalAlign.Center,
            )
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_up),
                contentDescription = null,
                tint = Color.Unspecified,
            )
        }
    )
    Box(
        modifier = Modifier
            .height(25.dp)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .horizontalScroll(
                    rememberScrollState()
                )
        ) {
            Text(
                text = annotatedString,
                inlineContent = inlineContent,
                fontFamily = Inter,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray,
                softWrap = false,
                maxLines = 1,
            )
        }
    }

}