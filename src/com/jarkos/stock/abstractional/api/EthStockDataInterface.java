package com.jarkos.stock.abstractional.api;

public interface EthStockDataInterface extends GeneralStockDataInterface {

    default String getType() {
        return "ETH";
    }

    Object getEthEurStockData();

}
