package com.jarkos.stock.dto.bitbay;

import com.jarkos.stock.abstractional.api.DashStockDataInterface;
import com.jarkos.stock.dto.bitbay.general.BitBayStockData;

import java.math.BigDecimal;

/**
 * Created by jkostrzewa on 2017-09-23.
 */
public class BitBayDashStockData extends BitBayStockData implements DashStockDataInterface {

    public BitBayDashStockData(BitBayStockData b) {
        super(b.getMax(), b.getMin(), b.getLast(), b.getBid(), b.getAsk(), b.getVwap(), b.getAverage(), b.getVolume(), b.getBids(), b.getAsks(), b.getTransactions());
    }

    @Override
    public BigDecimal getLastDashPrice() {
        return BigDecimal.valueOf(getLast());
    }

    @Override
    public BigDecimal getAskPrice() {
        return BigDecimal.valueOf(getAsk());

    }

    @Override
    public Object getDashEurStockData() {
        return this;
    }
}
