package com.jarkos.stock.enums;

/**
 * Created by jkostrzewa on 2017-09-20.
 */
public enum BccCurrencyPairEnum {

    BCCPLN("BCC/PLN"),
    BCCEUR("BCC/EUR"),
    BCCUSD("BCC/USD"),
    BCCETH("BCC/ETH"),
    BCCBTC("BCC/BTC"),
    BCCLTC("BCC/LTC"),

    BCHEUR("BCH/EUR"),
    BCHUSD("BCH/USD"),
    BCHETH("BCH/ETH"),
    BCHBTC("BCH/BTC"),
    BCHLTC("BCH/LTC");

    private String value;

    BccCurrencyPairEnum(String s) {
        this.value = s;
    }

    public String toString() {
        return this.value;
    }
}
