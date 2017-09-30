package com.jarkos.stock.enums.currencies;

/**
 * Created by jkostrzewa on 2017-09-28.
 */
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
