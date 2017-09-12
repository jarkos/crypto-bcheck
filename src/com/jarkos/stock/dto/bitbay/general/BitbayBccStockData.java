package com.jarkos.stock.dto.bitbay.general;

import com.jarkos.stock.abstractional.api.BccStockDataInterface;

import java.math.BigDecimal;

/**
 * Created by jkostrzewa on 2017-09-11.
 */
public class BitbayBccStockData extends BitbayStockData implements BccStockDataInterface {

    public BitbayBccStockData(BitbayStockData b) {
        super(b.getMax(), b.getMin(), b.getLast(), b.getBid(), b.getAsk(), b.getVwap(), b.getAverage(), b.getVolume(), b.getBids(), b.getAsks(), b.getTransactions());
    }

    @Override
    public BigDecimal getLastBccPrice() {
        return BigDecimal.valueOf(getLast());
    }
}
