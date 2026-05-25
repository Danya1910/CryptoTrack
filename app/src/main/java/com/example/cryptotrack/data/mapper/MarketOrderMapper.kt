package com.example.cryptotrack.data.mapper

import com.example.cryptotrack.domain.util.MarketOrder

fun MarketOrder.toApiValue(): String? {

    return when(this) {

        MarketOrder.DEFAULT ->
            null

        MarketOrder.MARKET_CAP_DESC ->
            "market_cap_desc"

        MarketOrder.MARKET_CAP_ASC ->
            "market_cap_asc"

        MarketOrder.VOLUME_DESC ->
            "volume_desc"

        MarketOrder.VOLUME_ASC ->
            "volume_asc"

        MarketOrder.PRICE_CHANGE_DESC ->
            "price_change_percentage_24h_desc"

        MarketOrder.PRICE_CHANGE_ASC ->
            "price_change_percentage_24h_asc"
    }
}