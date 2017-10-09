package com.jarkos.stock.dto.bitstamp;

import com.jarkos.stock.abstractional.api.DashStockDataInterface;
import com.jarkos.stock.dto.bitstamp.general.BitstampStockData;

public class BitstampDashStockData extends BitstampStockData implements DashStockDataInterface {

    @Override
    public Object getDashEurStockData() {
        return this;
    }
}
