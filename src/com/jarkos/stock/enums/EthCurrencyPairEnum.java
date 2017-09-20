package com.jarkos.stock.enums;

/**
 * Created by jkostrzewa on 2017-09-20.
 */
public enum EthCurrencyPairEnum {
    ETHPLN("ETH/PLN"),
    ETHEUR("ETH/EUR"),
    ETHUSD("ETH/USD");

    private String value;

    EthCurrencyPairEnum(String s) {
        this.value = s;
    }

    public String toString() {
        return this.value;
    }


}
