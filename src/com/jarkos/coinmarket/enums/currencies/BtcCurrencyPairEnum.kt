package com.jarkos.coinmarket.enums.currencies

enum class BtcCurrencyPairEnum constructor(private val value: String) {
    BTCPLN("BTC/PLN"),
    BTCEUR("BTC/EUR"),
    BTCUSD("BTC/USD"),
    BTCUSDT("BTC/USDT");

    override fun toString(): String {
        return this.value
    }
}
