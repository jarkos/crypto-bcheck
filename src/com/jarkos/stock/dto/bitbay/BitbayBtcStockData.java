package com.jarkos.stock.dto.bitbay;

import com.jarkos.stock.abstractional.api.BtcStockDataInterface;
import com.jarkos.stock.dto.bitbay.general.BitbayStockData;

import java.math.BigDecimal;

/**
 * Created by jkostrzewa on 2017-09-10.
 */
public class BitbayBtcStockData extends BitbayStockData implements BtcStockDataInterface {

    @Override
    public BigDecimal getLastBtcPrice() {
        return BigDecimal.valueOf(this.getBid());
    }
}
