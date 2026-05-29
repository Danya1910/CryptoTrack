package com.example.cryptotrack.presentation.util.price

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

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