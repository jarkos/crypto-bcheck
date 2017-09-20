package com.jarkos.stock.enums;

/**
 * Created by jkostrzewa on 2017-09-19.
 */
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
