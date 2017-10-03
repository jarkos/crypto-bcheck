package com.jarkos.stock.dto.bitbay;

import com.jarkos.stock.abstractional.api.BccStockDataInterface;
import com.jarkos.stock.dto.bitbay.general.BitBayStockData;

import java.math.BigDecimal;

public class BitBayBccStockData extends BitBayStockData implements BccStockDataInterface {

    public BitBayBccStockData(BitBayStockData b) {
        super(b.getMax(), b.getMin(), b.getLast(), b.getBid(), b.getAsk(), b.getVwap(), b.getAverage(), b.getVolume(), b.getBids(), b.getAsks(), b.getTransactions());
    }

    @Override
    public BigDecimal getLastBccPrice() {
        return BigDecimal.valueOf(getLast());
    }

    @Override
    public BigDecimal getAskBccPrice() {
        return BigDecimal.valueOf(getAsk());
    }
}
