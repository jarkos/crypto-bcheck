package pl.jarkos.backend.coinmarket.enums.currencies;

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
