package com.jarkos.coinmarket.enums;

public enum StockNameEnum {

    BitBay("BitBay"),
    Bitfinex("Bitfinex"),
    Bitstamp("Bitstamp"),
    GDAX("GDAX"),
    Kraken("Kraken"),
    Bittrex("Bittrex"),
    Gatecoin("Gatecoin"),
    Poloniex("Poloniex"),
    Coinroom("Coinroom"),
    GetBTC("GetBTC"),
    CoinsBank("CoinsBank"),
    TheRockTrading("The Rock Trading"),
    LitBit("LiteBit.eu"),
    Exmo("Exmo");

    private String value;

    StockNameEnum(String s) {
        this.value = s;
    }

    public String toString() {
        return this.value;
    }

}
