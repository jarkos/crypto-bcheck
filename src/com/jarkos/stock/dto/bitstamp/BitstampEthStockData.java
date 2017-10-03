package com.jarkos.stock.dto.bitstamp;

import com.jarkos.stock.abstractional.api.EthStockDataInterface;
import com.jarkos.stock.dto.bitstamp.general.BitstampStockData;

import java.math.BigDecimal;

/**
 * Created by jkostrzewa on 2017-09-22.
 */
public class BitstampEthStockData extends BitstampStockData implements EthStockDataInterface {

    public BitstampEthStockData(BitstampStockData b) {
        super(b.getHigh(), b.getLast(), b.getTimestamp(), b.getBid(), b.getVwap(), b.getVolume(), b.getLow(), b.getAsk(), b.getOpen());
    }

    @Override
    public BigDecimal getLastEthPrice() {
        return BigDecimal.valueOf(Float.valueOf(getLast()));
    }

    @Override
    public BigDecimal getAskEthPrice() {
        return BigDecimal.valueOf(Float.valueOf(getAsk()));
    }

    @Override
    public Object getEthEurStockData() {
        return this;
    }
}
