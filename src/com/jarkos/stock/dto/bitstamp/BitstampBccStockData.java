package com.jarkos.stock.dto.bitstamp;

import com.jarkos.stock.abstractional.api.BccStockDataInterface;
import com.jarkos.stock.dto.bitstamp.general.BitstampStockData;

import java.math.BigDecimal;

/**
 * Created by jkostrzewa on 2017-09-30.
 */
public class BitstampBccStockData extends BitstampStockData implements BccStockDataInterface {

    public BitstampBccStockData(BitstampStockData b) {
        super(b.getHigh(), b.getLast(), b.getTimestamp(), b.getBid(), b.getVwap(), b.getVolume(), b.getLow(), b.getAsk(), b.getOpen());
    }

    @Override
    public BigDecimal getLastBccPrice() {
        return BigDecimal.valueOf(Float.valueOf(getLast()));
    }

    @Override
    public BigDecimal getAskBccPrice() {
        return BigDecimal.valueOf(Float.valueOf(getAsk()));
    }
}
