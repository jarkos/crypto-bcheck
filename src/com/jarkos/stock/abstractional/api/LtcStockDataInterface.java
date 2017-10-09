package com.jarkos.stock.abstractional.api;

public interface LtcStockDataInterface extends GeneralStockDataInterface {

    default String getType() {
        return "LTC";
    }

    Object getLtcEurStockData();

}
