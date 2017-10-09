package com.jarkos.coinmarket.enums.currencies;

public enum GameCurrencyPairEnum {

    GAMEPLN("GAME/PLN"),
    GAMEEUR("GAME/EUR"),
    GAMEUSD("GAME/USD"),
    GAMEUSDT("GAME/USDT"),
    GAMEBTC("GAME/BTC"),
    GAMELTC("GAME/LTC");

    private String value;

    GameCurrencyPairEnum(String s) {
        this.value = s;
    }

    public String getCode(GameCurrencyPairEnum s) {
        return s.value;
    }

    public String toString() {
        return this.value;
    }

}
