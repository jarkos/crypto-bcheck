package pl.jarkos.backend.coinmarket.enums.currencies;

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
