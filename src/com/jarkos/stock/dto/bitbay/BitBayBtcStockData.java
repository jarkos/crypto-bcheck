package com.jarkos.stock.dto.bitbay;

import com.jarkos.stock.abstractional.api.BtcStockDataInterface;
import com.jarkos.stock.dto.bitbay.general.BitBayStockData;

import java.math.BigDecimal;

/**
 * Created by jkostrzewa on 2017-09-10.
 */
public class BitBayBtcStockData extends BitBayStockData implements BtcStockDataInterface {


    public BitBayBtcStockData(BitBayStockData b) {
        super(b.getMax(), b.getMin(), b.getLast(), b.getBid(), b.getAsk(), b.getVwap(), b.getAverage(), b.getVolume(), b.getBids(), b.getAsks(), b.getTransactions());
    }

    @Override
    public BigDecimal getLastBtcPrice() {
        return BigDecimal.valueOf(this.getLast());
    }

    @Override
    public BigDecimal getAskPrice() {
        return BigDecimal.valueOf(this.getAsk());
    }

    @Override
    public BigDecimal getBidPrice() {
        return BigDecimal.valueOf(this.getBid());
    }

    @Override
    public Object getBtcEurStockData() {
        return this;
    }

}
