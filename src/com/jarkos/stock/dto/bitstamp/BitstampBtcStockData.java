package com.jarkos.stock.dto.bitstamp;

import com.jarkos.stock.abstractional.api.BtcStockDataInterface;
import com.jarkos.stock.dto.bitstamp.general.BitstampStockData;

import java.math.BigDecimal;

public class BitstampBtcStockData extends BitstampStockData implements BtcStockDataInterface {

    public BitstampBtcStockData(BitstampStockData b) {
        super(b.getHigh(), b.getLast(), b.getTimestamp(), b.getBid(), b.getVwap(), b.getVolume(), b.getLow(), b.getAsk(), b.getOpen());
    }

    @Override
    public BigDecimal getLastBtcPrice() {
        return BigDecimal.valueOf(Float.valueOf(getLast()));
    }

    @Override
    public BigDecimal getAskPrice() {
        return BigDecimal.valueOf(Float.valueOf(getAsk()));
    }

    @Override
    public BigDecimal getBidPrice() {
        return BigDecimal.valueOf(Float.valueOf(getBid()));
    }

    @Override
    public BtcStockDataInterface getBtcEurStockData() {
        return this;
    }
}
