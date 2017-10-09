package com.jarkos.stock.dto.coinroom;

import com.jarkos.stock.abstractional.api.DashStockDataInterface;

public class CoinroomDashStockData extends CoinroomStockData implements DashStockDataInterface {

    public CoinroomDashStockData(CoinroomStockData c) {
        super(c.getLow(), c.getHigh(), c.getVwap(), c.getVolume(), c.getLast(), c.getAsk(), c.getBid());
    }

    @Override
    public Object getDashEurStockData() {
        return this;
    }

}
