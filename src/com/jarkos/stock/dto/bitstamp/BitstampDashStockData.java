package com.jarkos.stock.dto.bitstamp;

import com.jarkos.stock.abstractional.api.DashStockDataInterface;
import com.jarkos.stock.dto.bitstamp.general.BitstampStockData;

import java.math.BigDecimal;

/**
 * Created by jkostrzewa on 2017-09-23.
 */
public class BitstampDashStockData extends BitstampStockData implements DashStockDataInterface {
    @Override
    public BigDecimal getLastDashPrice() {
        return BigDecimal.valueOf(Float.valueOf(getLast()));
    }

    @Override
    public BigDecimal getAskPrice() {
        return BigDecimal.valueOf(Float.valueOf(getAsk()));

    }

    @Override
    public Object getDashEurStockData() {
        return this;
    }
}
