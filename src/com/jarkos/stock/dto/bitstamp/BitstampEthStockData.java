package com.jarkos.stock.dto.bitstamp;

import com.jarkos.stock.abstractional.api.EthStockDataInterface;
import com.jarkos.stock.dto.bitstamp.general.BitstampStockData;

public class BitstampEthStockData extends BitstampStockData implements EthStockDataInterface {

    public BitstampEthStockData(BitstampStockData b) {
        super(b.getHigh(), b.getLast(), b.getTimestamp(), b.getBid(), b.getVwap(), b.getVolume(), b.getLow(), b.getAsk(), b.getOpen());
    }

    @Override
    public Object getEthEurStockData() {
        return this;
    }
}
