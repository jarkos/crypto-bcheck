package com.jarkos.stock.dto.coinroom;

import com.jarkos.stock.abstractional.api.EthStockDataInterface;

import java.math.BigDecimal;

public class CoinroomEthStockStockData extends CoinroomStockData implements EthStockDataInterface {

    public CoinroomEthStockStockData(CoinroomStockData c) {
        super(c.getLow(), c.getHigh(), c.getVwap(), c.getVolume(), c.getLast(), c.getAsk(), c.getBid());
    }

    @Override
    public BigDecimal getLastEthPrice() {
        return BigDecimal.valueOf(getLast());
    }

    @Override
    public BigDecimal getAskEthPrice() {
        return BigDecimal.valueOf(getAsk());
    }

    @Override
    public EthStockDataInterface getEthEurStockData() {
        return this;
    }
}
