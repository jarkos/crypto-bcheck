package com.jarkos.stock.abstractional.api;

public interface BccStockDataInterface extends GeneralStockDataInterface {

    default String getType() {
        return "BCC";
    }

    Object getBccStockData();
}
