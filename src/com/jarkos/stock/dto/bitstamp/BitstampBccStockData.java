package com.jarkos.stock.dto.bitstamp;

import com.jarkos.stock.abstractional.api.BccStockDataInterface;
import com.jarkos.stock.dto.bitstamp.general.BitstampStockData;

public class BitstampBccStockData extends BitstampStockData implements BccStockDataInterface {

    public BitstampBccStockData(BitstampStockData b) {
        super(b.getHigh(), b.getLast(), b.getTimestamp(), b.getBid(), b.getVwap(), b.getVolume(), b.getLow(), b.getAsk(), b.getOpen());
    }

    @Override
    public Object getBccStockData() {
        return this;
    }
}
