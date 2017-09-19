package com.jarkos.stock.enums;

/**
 * Created by jkostrzewa on 2017-09-19.
 */
public enum CurrencyPairsEnum {
    BTCPLN("BTC/PLN"),
    BTCEUR("BTC/EUR"),
    BTCUSD("BTC/USD"),
    BTCUSDT("BTC/USDT");

    private String value;

    CurrencyPairsEnum(String s) {
        this.value = s;
    }

    public String toString() {
        return this.value;
    }
}
