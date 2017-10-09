package com.jarkos.stock.dto.coinroom;

import com.jarkos.stock.abstractional.api.LtcStockDataInterface;

public class CoinroomLtcStockData extends CoinroomStockData implements LtcStockDataInterface {

    public CoinroomLtcStockData(CoinroomStockData c) {
        super(c.getLow(), c.getHigh(), c.getVwap(), c.getVolume(), c.getLast(), c.getAsk(), c.getBid());
    }

    @Override
    public Object getLtcEurStockData() {
        return this;
    }
}
