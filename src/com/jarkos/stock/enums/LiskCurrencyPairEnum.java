package com.jarkos.stock.enums;

/**
 * Created by jkostrzewa on 2017-09-20.
 */
public enum LiskCurrencyPairEnum {

    LSKPLN("LSK/PLN"),
    LSKEUR("LSK/EUR"),
    LSKUSD("LSK/USD"),
    LSKETH("LSK/ETH"),
    LSKBTC("LSK/BTC"),
    LSKLTC("LSK/LTC");

    private String value;

    LiskCurrencyPairEnum(String s) {
        this.value = s;
    }

    public String toString() {
        return this.value;
    }

}
