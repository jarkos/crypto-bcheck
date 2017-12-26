package pl.jarkos.backend.coinmarket.enums.currencies;

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
