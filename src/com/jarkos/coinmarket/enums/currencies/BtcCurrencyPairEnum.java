package com.jarkos.coinmarket.enums.currencies;

public enum BtcCurrencyPairEnum {
    BTCPLN("BTC/PLN"),
    BTCEUR("BTC/EUR"),
    BTCUSD("BTC/USD"),
    BTCUSDT("BTC/USDT");

    private String value;

    BtcCurrencyPairEnum(String s) {
        this.value = s;
    }

    public String getCode(BtcCurrencyPairEnum s) {
        return s.value;
    }

    public String toString() {
        return this.value;
    }
}
