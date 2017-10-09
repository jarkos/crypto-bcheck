package com.jarkos.stock.dto.coinroom;

import com.jarkos.stock.abstractional.api.BccStockDataInterface;

public class CoinroomBccStockData extends CoinroomStockData implements BccStockDataInterface {

    public CoinroomBccStockData(CoinroomStockData c) {
        super(c.getLow(), c.getHigh(), c.getVwap(), c.getVolume(), c.getLast(), c.getAsk(), c.getBid());
    }

    @Override
    public Object getBccStockData() {
        return this;
    }
}
