package com.jarkos.stock.dto.bitbay;

import com.jarkos.stock.abstractional.api.LtcStockDataInterface;
import com.jarkos.stock.dto.bitbay.general.BitBayStockData;

import java.math.BigDecimal;

/**
 * Created by jkostrzewa on 2017-09-10.
 */
public class BitBayLtcStockData extends BitBayStockData implements LtcStockDataInterface {

    @Override
    public BigDecimal getLastLtcPrice() {
        return BigDecimal.valueOf(this.getBid());
    }

    @Override
    public Object getLtcEurStockData() {
        return this;
    }

}
