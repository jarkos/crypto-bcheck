package com.jarkos.stock.dto.coinroom;

import com.jarkos.stock.abstractional.api.BccStockDataInterface;

import java.math.BigDecimal;

public class CoinroomBccStockStockData extends CoinroomStockData implements BccStockDataInterface {

    public CoinroomBccStockStockData(CoinroomStockData c) {
        super(c.getLow(), c.getHigh(), c.getVwap(), c.getVolume(), c.getLast(), c.getAsk(), c.getBid());
    }

    @Override
    public BigDecimal getLastBccPrice() {
        return BigDecimal.valueOf(getLast());
    }

    @Override
    public BigDecimal getAskBccPrice() {
        return BigDecimal.valueOf(getAsk());
    }
}
