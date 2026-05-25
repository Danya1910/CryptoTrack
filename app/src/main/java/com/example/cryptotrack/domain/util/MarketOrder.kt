package com.example.cryptotrack.domain.util


enum class MarketOrder {
    DEFAULT,

    MARKET_CAP_DESC,
    MARKET_CAP_ASC,

    VOLUME_DESC,
    VOLUME_ASC,

    PRICE_CHANGE_DESC,
    PRICE_CHANGE_ASC,
}