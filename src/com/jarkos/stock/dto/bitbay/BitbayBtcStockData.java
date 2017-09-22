package com.jarkos.stock.dto.bitbay;

import com.jarkos.stock.abstractional.api.BtcStockDataInterface;
import com.jarkos.stock.dto.bitbay.general.BitbayStockData;

import java.math.BigDecimal;

/**
 * Created by jkostrzewa on 2017-09-10.
 */
public class BitbayBtcStockData extends BitbayStockData implements BtcStockDataInterface {


    public BitbayBtcStockData(BitbayStockData b) {
        super(b.getMax(), b.getMin(), b.getLast(), b.getBid(), b.getAsk(), b.getVwap(), b.getAverage(), b.getVolume(), b.getBids(), b.getAsks(), b.getTransactions());
    }

    @Override
    public BigDecimal getLastBtcPrice() {
        return BigDecimal.valueOf(this.getBid());
    }

    @Override
    public Object getBtcEurStockData() {
        return this;
    }

}
