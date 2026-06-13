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
                totalValue = totalValue,
                imageUrl = list.first().imageUrl,
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

fun getCoinPlural(value: Double): String {
    // Проверяем, есть ли значимая дробная часть (например, 1.5 или 0.1)
    val hasFraction = value % 1.0 != 0.0

    return if (hasFraction) {
        // Любая дробь -> родительный падеж, ед. число
        "монеты"
    } else {
        // Если число целое (1.0, 5.0), приводим к Long и склоняем по правилам целых чисел
        getCoinPluralLong(value.toLong())
    }
}

// Вспомогательная функция для склонения целых чисел
private fun getCoinPluralLong(value: Long): String {
    val absValue = Math.abs(value)
    val lastDigit = absValue % 10
    val lastTwoDigits = absValue % 100

    return when {
        lastTwoDigits in 11..19 -> "монет"
        lastDigit == 1L -> "монета"
        lastDigit in 2..4 -> "монеты"
        else -> "монет"
    }
}
