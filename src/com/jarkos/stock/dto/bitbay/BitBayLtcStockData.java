package com.jarkos.stock.dto.bitbay;

import com.jarkos.stock.abstractional.api.LtcStockDataInterface;
import com.jarkos.stock.dto.bitbay.general.BitBayStockData;

public class BitBayLtcStockData extends BitBayStockData implements LtcStockDataInterface {

    public BitBayLtcStockData(BitBayStockData b) {
        super(b.getMax(), b.getMin(), b.getLast(), b.getBid(), b.getAsk(), b.getVwap(), b.getAverage(), b.getVolume(), b.getBids(), b.getAsks(), b.getTransactions());
    }

    @Override
    public Object getLtcEurStockData() {
        return this;
    }

}
