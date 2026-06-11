package com.example.cryptotrack.presentation.util.price

import com.example.cryptotrack.domain.model.PurchaseCoin
import com.example.cryptotrack.presentation.util.uiModels.AggregatedPurchase
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun formatPrice(value: Double?): String {
    val v = value ?: return "0"

    val symbols = DecimalFormatSymbols().apply {
        groupingSeparator = ' '
        decimalSeparator = '.'
    }

    val pattern = when {
        v >= 1000 -> "#,##0.00"
        v >= 1 -> "#,##0.00"
        v >= 0.01 -> "#,##0.0000"
        v >= 0.0001 -> "#,##0.000000"
        else -> "#,##0.00000000"
    }

    return DecimalFormat(pattern, symbols).format(v)
}

fun Double.toCompactUsd(): String {
    return when {
        this >= 1_000_000_000_000 ->
            "$" + String.format(Locale.US, "%.2fT", this / 1_000_000_000_000)

        this >= 1_000_000_000 ->
            "$" + String.format(Locale.US, "%.2fB", this / 1_000_000_000)

        this >= 1_000_000 ->
            "$" + String.format(Locale.US, "%.2fM", this / 1_000_000)

        this >= 1_000 ->
            "$" + String.format(Locale.US, "%.2fK", this / 1_000)

        else ->
            "$" + String.format(Locale.US, "%.2f", this)
    }
}

fun sanitizePrice(input: String): String {
    val filtered = input
        .replace(",", ".") // заменяем запятую
        .filter { it.isDigit() || it == '.' }

    val parts = filtered.split('.')

    return when {
        parts.size <= 1 -> filtered
        else -> parts[0] + "." + parts.drop(1).joinToString("")
    }
}

fun sanitizeAmount(input: String): String {
    val filtered = input
        .replace(",", ".")
        .filter { it.isDigit() || it == '.' }

    val parts = filtered.split('.')

    return when {
        parts.size <= 1 -> filtered
        else -> parts[0] + "." + parts.drop(1).joinToString("")
    }
}

fun aggregatePurchases(purchases: List<PurchaseCoin>): List<AggregatedPurchase> {

    return purchases
        .groupBy { it.coinId }
        .map { (_, list) ->

            val totalAmount = list.sumOf { it.amount }
            val totalValue = list.sumOf { it.amount * it.buyPrice }

            AggregatedPurchase(
                coinId = list.first().coinId,
                name = list.first().name,
                totalAmount = totalAmount,
                totalValue = totalValue
            )
        }
}

fun formatTime(millis: Long): String {
    val formatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale("ru"))
        .withZone(ZoneId.systemDefault())

    return formatter.format(Instant.ofEpochMilli(millis))
}

fun formatTimeAndDate(millis: Long): String {
    val formatter = DateTimeFormatter.ofPattern("d MMM yyyy\nHH:mm", Locale("ru"))
        .withZone(ZoneId.systemDefault())

    return formatter.format(Instant.ofEpochMilli(millis))
}

fun Int?.formatWithSpaces(formatter: DecimalFormat): String {
    return this?.let { formatter.format(it) } ?: ""
}