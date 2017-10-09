package com.jarkos.stock.abstractional.api;

public interface DashStockDataInterface extends GeneralStockDataInterface {

    default String getType() {
        return "DASH";
    }

    Object getDashEurStockData();

}
