package com.jarkos.stock.dto.bitbay;

import com.jarkos.stock.abstractional.api.LtcStockDataInterface;
import com.jarkos.stock.dto.bitbay.general.BitBayStockData;

import java.math.BigDecimal;

/**
 * Created by jkostrzewa on 2017-09-10.
 */
public class BitBayLtcStockData extends BitBayStockData implements LtcStockDataInterface {

    public BitBayLtcStockData(BitBayStockData b) {
        super(b.getMax(), b.getMin(), b.getLast(), b.getBid(), b.getAsk(), b.getVwap(), b.getAverage(), b.getVolume(), b.getBids(), b.getAsks(), b.getTransactions());
    }

    @Override
    public BigDecimal getLastLtcPrice() {
        return BigDecimal.valueOf(this.getLast());
    }

    @Override
    public BigDecimal getAskPrice() {
        return BigDecimal.valueOf(this.getAsk());
    }

    @Override
    public Object getLtcEurStockData() {
        return this;
    }

}
