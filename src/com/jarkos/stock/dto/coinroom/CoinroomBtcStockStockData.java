package com.jarkos.stock.dto.coinroom;

import com.jarkos.stock.abstractional.api.BtcStockDataInterface;

import java.math.BigDecimal;

public class CoinroomBtcStockStockData extends CoinroomStockData implements BtcStockDataInterface {

    public CoinroomBtcStockStockData(CoinroomStockData c) {
        super(c.getLow(), c.getHigh(), c.getVwap(), c.getVolume(), c.getLast(), c.getAsk(), c.getBid());
    }

    @Override
    public BigDecimal getLastBtcPrice() {
        return BigDecimal.valueOf(getLast());
    }

    @Override
    public BigDecimal getAskPrice() {
        return BigDecimal.valueOf(getAsk());
    }

    @Override
    public BigDecimal getBidPrice() {
        return BigDecimal.valueOf(getBid());
    }

    @Override
    public BtcStockDataInterface getBtcEurStockData() {
        return this;
    }
}
