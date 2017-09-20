package com.jarkos.stock.enums;

/**
 * Created by jkostrzewa on 2017-09-19.
 */
public enum StockNameEnum {

    BitBay("BitBay"),
    Bitfinex("Bitfinex"),
    Bitstamp("Bitstamp"),
    GDAX("GDAX"),
    Kraken("Kraken"),
    Bittrex("Bittrex"),
    Gatecoin("Gatecoin"),
    LitBit("LiteBit.eu");

    private String value;

    StockNameEnum(String s) {
        this.value = s;
    }

    public String toString() {
        return this.value;
    }

}