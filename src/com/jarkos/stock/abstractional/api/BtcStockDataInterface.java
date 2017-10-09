package com.jarkos.stock.abstractional.api;

public interface BtcStockDataInterface extends GeneralStockDataInterface {

    default String getType() {
        return "BTC";
    }

    Object getBtcEurStockData();


}
