package com.jarkos.stock.dto.coinroom;

import com.jarkos.stock.abstractional.api.BtcStockDataInterface;

public class CoinroomBtcStockData extends CoinroomStockData implements BtcStockDataInterface {

    public CoinroomBtcStockData(CoinroomStockData c) {
        super(c.getLow(), c.getHigh(), c.getVwap(), c.getVolume(), c.getLast(), c.getAsk(), c.getBid());
    }

    @Override
    public BtcStockDataInterface getBtcEurStockData() {
        return this;
    }
}
