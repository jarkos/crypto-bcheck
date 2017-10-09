package com.jarkos.stock.dto.bitbay;

import com.jarkos.stock.abstractional.api.DashStockDataInterface;
import com.jarkos.stock.dto.bitbay.general.BitBayStockData;

public class BitBayDashStockData extends BitBayStockData implements DashStockDataInterface {

    public BitBayDashStockData(BitBayStockData b) {
        super(b.getMax(), b.getMin(), b.getLast(), b.getBid(), b.getAsk(), b.getVwap(), b.getAverage(), b.getVolume(), b.getBids(), b.getAsks(), b.getTransactions());
    }

    @Override
    public Object getDashEurStockData() {
        return this;
    }
}
