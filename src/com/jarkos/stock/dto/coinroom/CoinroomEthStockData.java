package com.jarkos.stock.dto.coinroom;

import com.jarkos.stock.abstractional.api.EthStockDataInterface;

public class CoinroomEthStockData extends CoinroomStockData implements EthStockDataInterface {

    public CoinroomEthStockData(CoinroomStockData c) {
        super(c.getLow(), c.getHigh(), c.getVwap(), c.getVolume(), c.getLast(), c.getAsk(), c.getBid());
    }

    @Override
    public EthStockDataInterface getEthEurStockData() {
        return this;
    }
}
