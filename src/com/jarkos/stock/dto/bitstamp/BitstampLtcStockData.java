package com.jarkos.stock.dto.bitstamp;

import com.jarkos.stock.abstractional.api.LtcStockDataInterface;
import com.jarkos.stock.dto.bitstamp.general.BitstampStockData;

public class BitstampLtcStockData extends BitstampStockData implements LtcStockDataInterface {

    public BitstampLtcStockData(BitstampStockData b) {
        super(b.getHigh(), b.getLast(), b.getTimestamp(), b.getBid(), b.getVwap(), b.getVolume(), b.getLow(), b.getAsk(), b.getOpen());
    }

    @Override
    public Object getLtcEurStockData() {
        return this;
    }

}
