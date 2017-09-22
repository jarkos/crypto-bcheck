package com.jarkos.stock.dto.bitbay;

import com.jarkos.stock.abstractional.api.EthStockDataInterface;
import com.jarkos.stock.dto.bitbay.general.BitBayStockData;

import java.math.BigDecimal;

/**
 * Created by jkostrzewa on 2017-09-22.
 */
public class BitBayEthStockData extends BitBayStockData implements EthStockDataInterface {

    public BitBayEthStockData(BitBayStockData b) {
        super(b.getMax(), b.getMin(), b.getLast(), b.getBid(), b.getAsk(), b.getVwap(), b.getAverage(), b.getVolume(), b.getBids(), b.getAsks(), b.getTransactions());
    }

    @Override
    public BigDecimal getLastEthPrice() {
        return BigDecimal.valueOf(this.getBid());
    }

    @Override
    public Object getEthEurStockData() {
        return this;
    }
}
