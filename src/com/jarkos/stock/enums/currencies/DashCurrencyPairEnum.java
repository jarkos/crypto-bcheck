package com.jarkos.stock.enums.currencies;

/**
 * Created by jkostrzewa on 2017-09-22.
 */
public enum DashCurrencyPairEnum {

    DASHPLN("DASH/PLN"),
    DASHEUR("DASH/EUR"),
    DASHUSD("DASH/USD"),
    DASHUSDT("DASH/USDT");

    private String value;

    DashCurrencyPairEnum(String s) {
        this.value = s;
    }

    public String getCode(DashCurrencyPairEnum s) {
        return s.value;
    }

    public String toString() {
        return this.value;
    }

}
