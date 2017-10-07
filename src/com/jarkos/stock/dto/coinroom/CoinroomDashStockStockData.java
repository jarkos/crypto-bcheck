package com.jarkos.stock.dto.coinroom;

import com.jarkos.stock.abstractional.api.DashStockDataInterface;

import java.math.BigDecimal;

public class CoinroomDashStockStockData extends CoinroomStockData implements DashStockDataInterface {


    public CoinroomDashStockStockData(CoinroomStockData c) {
        super(c.getLow(), c.getHigh(), c.getVwap(), c.getVolume(), c.getLast(), c.getAsk(), c.getBid());
    }

    @Override
    public BigDecimal getLastDashPrice() {
        return BigDecimal.valueOf(getLast());
    }

    @Override
    public BigDecimal getAskPrice() {
        return BigDecimal.valueOf(getAsk());
    }

    @Override
    public Object getDashEurStockData() {
        return this;
    }

}
