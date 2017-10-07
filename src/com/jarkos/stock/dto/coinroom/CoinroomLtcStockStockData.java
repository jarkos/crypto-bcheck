package com.jarkos.stock.dto.coinroom;

import com.jarkos.stock.abstractional.api.LtcStockDataInterface;

import java.math.BigDecimal;

public class CoinroomLtcStockStockData extends CoinroomStockData implements LtcStockDataInterface {

    public CoinroomLtcStockStockData(CoinroomStockData c) {
        super(c.getLow(), c.getHigh(), c.getVwap(), c.getVolume(), c.getLast(), c.getAsk(), c.getBid());
    }

    @Override
    public BigDecimal getLastLtcPrice() {
        return BigDecimal.valueOf(getLast());
    }

    @Override
    public BigDecimal getAskPrice() {
        return BigDecimal.valueOf(getAsk());
    }

    @Override
    public Object getLtcEurStockData() {
        return this;
    }
}
