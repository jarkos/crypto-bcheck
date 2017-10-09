package com.jarkos.coinmarket.enums.currencies;

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
