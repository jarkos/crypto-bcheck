package com.jarkos.stock.dto.bitbay;

import com.jarkos.stock.dto.bitbay.general.BitBayStockData;

/**
 * Created by jkostrzewa on 2017-09-23.
 */
public class BitBayDashStockData extends BitBayStockData {

    public BitBayDashStockData(BitBayStockData b) {
        super(b.getMax(), b.getMin(), b.getLast(), b.getBid(), b.getAsk(), b.getVwap(), b.getAverage(), b.getVolume(), b.getBids(), b.getAsks(), b.getTransactions());
    }

}
