package com.jarkos.stock.dto.bitbay;

import com.jarkos.stock.abstractional.api.LtcStockDataInterface;
import com.jarkos.stock.dto.bitbay.general.BitbayStockData;

import java.math.BigDecimal;

/**
 * Created by jkostrzewa on 2017-09-10.
 */
public class BitbayLtcStockData extends BitbayStockData implements LtcStockDataInterface {

    @Override
    public BigDecimal getLastLtcPrice() {
        return BigDecimal.valueOf(this.getBid());
    }

    @Override
    public Object getLtcEurStockData() {
        return this;
    }

}
