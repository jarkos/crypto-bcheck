package com.jarkos.stock.enums;

/**
 * Created by jkostrzewa on 2017-09-20.
 */
public enum LtcCurrencyPairEnum {
    LTCPLN("LTC/PLN"),
    LTCEUR("LTC/EUR"),
    LTCUSD("LTC/USD"),
    LTCUSDT("LTC/USDT");

    private String value;

    LtcCurrencyPairEnum(String s) {
        this.value = s;
    }

    public String toString() {
        return this.value;
    }

}
